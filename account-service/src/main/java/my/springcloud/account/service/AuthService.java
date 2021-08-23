package my.springcloud.account.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.domain.aggregate.Account;
import my.springcloud.account.domain.entity.LoginHistory;
import my.springcloud.account.domain.repository.AccountRepository;
import my.springcloud.account.domain.repository.LoginHistoryRepository;
import my.springcloud.account.exception.AuthException;
import my.springcloud.common.api.AuthApiClient;
import my.springcloud.common.api.LogoutApiClient;
import my.springcloud.common.constants.AccountStatusType;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.exception.ResourceNotFoundException;
import my.springcloud.common.exception.ServiceException;
import my.springcloud.common.model.auth.AuthCheck;
import my.springcloud.common.model.auth.LoginCheck;
import my.springcloud.common.model.auth.PasswordCheck;
import my.springcloud.common.model.auth.PasswordUpdate;
import my.springcloud.common.model.auth.TokenDetail;
import my.springcloud.common.sec.model.CustomUserDetails;
import my.springcloud.common.sec.model.CustomUserDetailsHelper;
import my.springcloud.common.wrapper.CommonModel;

@Slf4j
@RequiredArgsConstructor
@RefreshScope
@Service
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;
	private final LoginHistoryRepository loginHistoryRepository;

	private final AuthApiClient authApiClient;
	private final LogoutApiClient logoutApiClient;

	@Value("${spring.profiles.active}")
	private String springProfilesActive;

	private void checkAccountStatus(String status) {
		if (AccountStatusType.BLOCK.code().equalsIgnoreCase(status)) { // 차단된 계정
			throw new AuthException(ResponseCodeType.SERVER_ERROR_41001006);
		} else if (AccountStatusType.LOCKED.code().equalsIgnoreCase(status)) { // 잠긴 계정
			throw new AuthException(ResponseCodeType.SERVER_ERROR_41001009);
		}
	}

	@Transactional(noRollbackFor = {AuthException.class})
	public String login(LoginCheck dto) {
		Account account = this.accountRepository.findByUsername(dto.getUsername())
			.orElseThrow(ResourceNotFoundException::new);

		this.checkAccountStatus(account.getStatus());

		LoginHistory loginHistory = this.loginHistoryRepository.findTop1ByAccountIdOrderByHistorySeqDesc(
			account.getAccountId())
			.filter(h -> Objects.nonNull(h.getLoginFailCnt()) && (h.getLoginFailCnt() > 0 && h.getLoginFailCnt() < 5))
			.orElseGet(LoginHistory::new);

		String authToken;
		if (Objects.isNull(loginHistory.getHistorySeq())) { // 신규 로그인
			loginHistory.setAccountId(account.getAccountId());
			loginHistory.setLoginFailCnt(0);
			loginHistory.setOtpAuthTryCnt(0);
			loginHistory.setOtpAuthFailCnt(0);

			authToken = UUID.randomUUID().toString();
			loginHistory.setAuthToken(authToken);
			loginHistory.setLoginReqDt(LocalDateTime.now());

			this.loginHistoryRepository.save(loginHistory);
		} else { // 로그인 실패 후 재로그인
			authToken = loginHistory.getAuthToken();
		}

		if (!this.passwordEncoder.matches(dto.getPassword(), account.getPassword())) {
			int loginFailCnt = loginHistory.getLoginFailCnt() + 1;
			loginHistory.setLoginFailCnt(loginFailCnt);

			if (loginFailCnt >= 5) { // 로그인 5회 실패 시 계정 차단
				account.setStatus(AccountStatusType.BLOCK.code());
				account.setUpdId(dto.getUsername());
				account.setUpdDt(LocalDateTime.now());

				throw new AuthException(ResponseCodeType.SERVER_ERROR_41001011); // 로그인 실패 횟수 초과
			}

			throw new AuthException(ResponseCodeType.SERVER_ERROR_41001001); // 인증 실패
		}

		return authToken;
	}

	@Transactional(noRollbackFor = {AuthException.class})
	public boolean requestSmsOtp(String authToken) {
		LoginHistory loginHistory = this.loginHistoryRepository.findTop1ByAuthTokenOrderByHistorySeqDesc(authToken)
			.orElseThrow(ResourceNotFoundException::new);
		Account account = this.accountRepository.findById(loginHistory.getAccountId())
			.orElseThrow(ResourceNotFoundException::new);

		this.checkAccountStatus(account.getStatus());

		loginHistory.setLoginFailCnt(0);

		int otpAuthTryCnt = loginHistory.getOtpAuthTryCnt();
		loginHistory.setOtpAuthTryCnt(++otpAuthTryCnt);

		if (otpAuthTryCnt % 10 == 0)
			loginHistory.setLoginReqDt(LocalDateTime.now());

		long diff = ChronoUnit.SECONDS.between(loginHistory.getLoginReqDt(), LocalDateTime.now());

		if (otpAuthTryCnt > 10 && diff <= 60) {
			log.debug("> OTP인증시도건수: {}, ({}) 계정잠금", otpAuthTryCnt, account.getUsername());
			LocalDateTime nowDatetime = LocalDateTime.now();
			account.setStatus(AccountStatusType.LOCKED.code());
			account.setUpdId(account.getUsername());
			account.setUpdDt(nowDatetime);
			account.setAccountLockedDt(nowDatetime);

			throw new AuthException(ResponseCodeType.SERVER_ERROR_41001010);
		}

		String otp = RandomStringUtils.randomNumeric(6);
		log.debug("> OTP: {}", otp);

		if (!"prd".equalsIgnoreCase(this.springProfilesActive)) {
			otp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
			log.debug("> now is no prod envirionment, so OTP is today date: {}", otp);
		}

		loginHistory.setOtp(otp);

		// 신규 OTP 발급 시, 이전 발급 OTP 파기
		List<LoginHistory> loginHistories = this.loginHistoryRepository.findByAccountIdAndOtpNotNull(
			loginHistory.getAccountId());
		loginHistories.stream()
			.filter(h -> h.getHistorySeq() < loginHistory.getHistorySeq())
			.forEach(h -> {
				h.setOtpAuthTryCnt(0);
				h.setOtpAuthFailCnt(0);
				h.setOtp(null);
			});

		// OTP SMS 발송
		//        OtpSmsSendDto dto = new OtpSmsSendDto(otp, account.getPhoneNumber());
		//        CommonModel<Boolean> cm = this.uplusApiClient.sendOtp(dto);
		//
		//        return cm.getResult();
		return true;
	}

	@Transactional
	public TokenDetail refreshAccessToken(String refreshToken) {
		CustomUserDetailsHelper tokenHelper = new CustomUserDetailsHelper();
		CustomUserDetails admin;

		try {
			admin = tokenHelper.getSimpleUser(refreshToken);
		} catch (ExpiredJwtException e) {
			throw new ServiceException(ResponseCodeType.SERVER_ERROR_41001002, e);
		} catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			throw new ServiceException(ResponseCodeType.SERVER_ERROR_41001001, e);
		}

		String newAccessToken = tokenHelper.generateAccessToken(admin);
		String newRefreshToken = tokenHelper.generateRefreshToken(admin);

		return new TokenDetail(newAccessToken, newRefreshToken);
	}

	@Transactional(noRollbackFor = {AuthException.class})
	public Account checkLoginHistory(String authToken, String otp) {
		LoginHistory loginHistory = this.loginHistoryRepository.findTop1ByAuthTokenOrderByHistorySeqDesc(authToken)
			.orElseThrow(() -> new AuthException(ResponseCodeType.SERVER_ERROR_41001001));
		Account account = this.accountRepository.findById(loginHistory.getAccountId())
			.orElseThrow(() -> new AuthException(ResponseCodeType.SERVER_ERROR_41001002));

		this.checkAccountStatus(account.getStatus());

		int otpAuthFailCnt = loginHistory.getOtpAuthFailCnt();

		if (loginHistory.getLoginReqDt().isBefore(LocalDateTime.now().minusMinutes(5L))) { // 5분 초과
			loginHistory.setOtpAuthFailCnt(++otpAuthFailCnt);

			this.checkIfAccountLocked(otpAuthFailCnt, account);

			throw new AuthException(ResponseCodeType.SERVER_ERROR_41001007);
		}

		if (Objects.isNull(loginHistory.getOtp()) || !loginHistory.getOtp().equals(otp)) { // OTP 불일치
			loginHistory.setOtpAuthFailCnt(++otpAuthFailCnt);

			this.checkIfAccountLocked(otpAuthFailCnt, account);

			throw new AuthException(ResponseCodeType.SERVER_ERROR_41001008);
		}

		return account;
	}

	private void checkIfAccountLocked(int failCnt, Account account) {
		if (failCnt >= 5) { // OTP인증실패건수 5회 이상 초과이면 계정 잠김
			log.debug("> OTP인증실패건수: {}, ({}) 계정잠금", failCnt, account.getUsername());
			LocalDateTime nowDatetime = LocalDateTime.now();
			account.setStatus(AccountStatusType.LOCKED.code());
			account.setUpdId(account.getUsername());
			account.setUpdDt(nowDatetime);
			account.setAccountLockedDt(nowDatetime);
		}
	}

	@Transactional
	public boolean updateLoginDtAndCheckNonExpiredPassword(Long accountId) {
		LoginHistory loginHistory = this.loginHistoryRepository.findTop1ByAccountIdOrderByHistorySeqDesc(accountId)
			.orElseThrow(() -> new UsernameNotFoundException(accountId + " not exists."));
		loginHistory.setLoginDt(LocalDateTime.now());

		Account account = this.accountRepository.getOne(loginHistory.getAccountId());
		LocalDateTime passwordUpdDt =
			(account.getPasswordUpdDt() == null) ? account.getRegDt() : account.getPasswordUpdDt();

		return !passwordUpdDt.isBefore(LocalDateTime.now().minusDays(90L));
	}

	@Transactional
	public boolean changePassword(CustomUserDetails admin, PasswordUpdate dto) {
		Account account = this.accountRepository.findById(admin.getAccountId())
			.orElseThrow(ResourceNotFoundException::new);

		if (this.passwordEncoder.matches(dto.getNewPassword(), account.getPassword())) { // 이전 비밀번호와 동일 비번 사용불가
			throw new AuthException(ResponseCodeType.SERVER_ERROR_41001012);
		}

		if (dto.getNewPassword().contains(account.getUsername())) { // ID를 포함한 비번 사용불가
			throw new AuthException(ResponseCodeType.SERVER_ERROR_41001013);
		}

		if (!this.passwordEncoder.matches(dto.getNowPassword(), account.getPassword())) { // 현재 비번 불일치
			throw new AuthException(ResponseCodeType.SERVER_ERROR_41001001);
		}

		PasswordValidator.validatePassword(dto.getNewPassword());

		account.setPassword(this.passwordEncoder.encode(dto.getNewPassword()));
		account.setPasswordUpdDt(LocalDateTime.now());

		return true;
	}

	@Transactional(readOnly = true)
	public String checkPassword(CustomUserDetails admin, PasswordCheck dto) {
		Account account = this.accountRepository.findById(admin.getAccountId())
			.orElseThrow(ResourceNotFoundException::new);

		if (this.passwordEncoder.matches(dto.getNowPassword(), account.getPassword())) {
			String reconfirmToken = this.passwordEncoder.encode(String.valueOf(admin.getAccountId()));
			log.debug("> reconfirmToken: {}", reconfirmToken);
			return reconfirmToken;
		} else {
			throw new AuthException(ResponseCodeType.SERVER_ERROR_41001001);
		}
	}

	public boolean checkReconfirmToken(CustomUserDetails admin, String tempToken) {
		try {
			boolean tf = this.passwordEncoder.matches(String.valueOf(admin.getAccountId()), tempToken);
			log.debug("> temp token 확인: accountId: {}, tempToken: {}, 매칭여부: {}", admin.getAccountId(), tempToken, tf);

			if (tf) {
				return true;
			}

			throw new AuthException(ResponseCodeType.SERVER_ERROR_41001002);
		} catch (IllegalArgumentException e) {
			log.error("ReconfirmToken Error", e);
			throw new AuthException(ResponseCodeType.SERVER_ERROR_41001002);
		} catch (Exception e) {
			log.error("> Invalid tempToken! token: {}, accountId: {}", tempToken, admin.getAccountId(), e);
			throw new AuthException(ResponseCodeType.SERVER_ERROR_41001002);
		}
	}

	public CommonModel<TokenDetail> authFinal(AuthCheck dto) {
		log.debug("> AuthCheckDto: {}", dto.toString());
		return this.authApiClient.authFinal(dto);
	}

	public Boolean logout() {
		CommonModel<Boolean> model = this.logoutApiClient.logout();
		return model.getResult();
	}

	@Transactional
	public void unlockAccount() {
		LocalDateTime datetimeBefore = LocalDateTime.now().minusMinutes(30L);
		log.debug("> 잠긴지 30분 지난 계정 해제, datetimeBefore: {}", datetimeBefore);

		List<Account> lockedAccounts = this.accountRepository.findByStatusAndAccountLockedDtBefore(
			AccountStatusType.LOCKED.code(), datetimeBefore);
		lockedAccounts.forEach(a -> {
			a.setStatus(AccountStatusType.APPROVAL.code());
			a.setAccountLockedDt(null);
		});
	}

}
