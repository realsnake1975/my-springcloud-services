package my.springcloud.account.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.domain.aggregate.Account;
import my.springcloud.account.domain.repository.AccountRepository;
import my.springcloud.account.domain.repository.AuthorityRepository;
import my.springcloud.account.domain.repository.LoginHistoryRepository;
import my.springcloud.account.domain.spec.AccountSpec;
import my.springcloud.account.mapper.AccountMapper;
import my.springcloud.common.constants.AccountStatusType;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.exception.ResourceNotFoundException;
import my.springcloud.common.exception.ServiceException;
import my.springcloud.common.model.account.AccountDetail;
import my.springcloud.common.model.account.AccountModify;
import my.springcloud.common.sec.model.CustomUserDetails;
import my.springcloud.common.utils.TextUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {
	private final PasswordEncoder passwordEncoder;

	private final AccountRepository accountRepository;
	private final AccountMapper accountMapper;

	private final AuthorityRepository authorityRepository;
	private final LoginHistoryRepository loginHistoryRepository;

	private final HttpServletRequest request;

	private Account findById(long id) {
		return this.accountRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
	}

	/**
	 * 계정 단건 조회
	 *
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public AccountDetail find(UserDetails userDetails, long id) {
		log.info("[REQ 계정 단건 조회] 사용자 ID: {}, url: {}", userDetails.getUsername(),
			this.request.getRequestURL().toString());

		Account account = this.findById(id);
		AccountDetail accountDetail = this.accountMapper.toDto(account);
		accountDetail.setPassword("");
		accountDetail.convertXss();

		log.info("[RES 계정 단건 조회] 사용자 ID: {}, url: {}", userDetails.getUsername(),
			this.request.getRequestURL().toString());
		return accountDetail;
	}

	/**
	 * 계정 목록 조회
	 *
	 * @param spec
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<AccountDetail> find(UserDetails userDetails, AccountSpec spec, Pageable pageable) {
		log.info("[REQ 계정 목록 조회] 사용자 ID: {}, url: {}", userDetails.getUsername(),
			this.request.getRequestURL().toString());

		Page<AccountDetail> accountPage = this.accountRepository.findAll(spec, pageable).map(a -> {
			AccountDetail accountDetail = this.accountMapper.toDto(a);
			accountDetail.setPassword("");
			accountDetail.convertXss();
			return accountDetail;
		});

		log.info("[RES 계정 목록 조회] 사용자 ID: {}, url: {}", userDetails.getUsername(),
			this.request.getRequestURL().toString());
		return accountPage;
	}

	/**
	 * 계정 등록
	 *
	 * @param userDetails
	 * @param accountCreate
	 * @return
	 */
	@Transactional
	public AccountDetail create(@AuthenticationPrincipal UserDetails userDetails, AccountDetail accountCreate) {
		log.info("[REQ 계정 등록] 사용자 ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());

		PasswordValidator.validatePassword(accountCreate.getPassword());

		CustomUserDetails loginUser = (CustomUserDetails)userDetails;
		accountCreate.setRegId(loginUser.getUsername());
		accountCreate.setUpdId(loginUser.getUsername());

		String encodedPassword = this.passwordEncoder.encode(accountCreate.getPassword());
		accountCreate.setPassword(encodedPassword);

		AccountDetail accountDetail = this.accountMapper.toDto(
			this.accountRepository.save(this.accountMapper.toEntity(accountCreate)));
		accountDetail.setPassword("");
		accountDetail.convertXss();

		log.info("[RES 계정 등록] 사용자 ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
		return accountDetail;
	}

	/**
	 * 계정 수정
	 *
	 * @param userDetails
	 * @param id
	 * @param accountModify
	 * @return
	 */
	@Transactional
	public AccountDetail modify(@AuthenticationPrincipal UserDetails userDetails, long id,
		AccountModify accountModify) {
		log.info("[REQ 계정 수정] 사용자 ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());

		PasswordValidator.validatePassword(accountModify.getPassword());

		CustomUserDetails loginUser = (CustomUserDetails)userDetails;
		Account account = this.findById(id);

		// 패스워드 수정 시 이전 패스워드와 동일하면 안됨
		if (this.passwordEncoder.matches(accountModify.getPassword(), account.getPassword())) {
			throw new ServiceException(ResponseCodeType.SERVER_ERROR_41001014);
		}

		if (!this.accountMapper.toDto(account).equals(accountModify)) {
			if (TextUtils.isNotEmpty(accountModify.getPassword())) {
				String encodedPassword = this.passwordEncoder.encode(accountModify.getPassword());
				accountModify.setPassword(encodedPassword);
				account.setPassword(accountModify.getPassword());
				account.setPasswordUpdDt(LocalDateTime.now());
			}

			account.setAuthority(this.authorityRepository.getOne(accountModify.getAuthority().getAuthorityId()));
			account.setPhoneNumber(accountModify.getPhoneNumber());
			account.setEmail(accountModify.getEmail());
			account.setUpdDt(LocalDateTime.now());
			account.setUpdId(loginUser.getUsername());
		}

		AccountDetail accountDetail = this.accountMapper.toDto(account);
		accountDetail.setPassword("");
		accountDetail.convertXss();

		log.info("[RES 계정 수정] 사용자 ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
		return accountDetail;
	}

	/**
	 * 계정 삭제
	 *
	 * @param userDetails
	 * @param ids
	 * @return
	 */
	@Transactional
	public boolean remove(UserDetails userDetails, List<Long> ids) {
		log.info("[REQ 계정 삭제] 사용자 ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());

		try {
			ids.forEach(this.accountRepository::deleteById);
			log.info("[RES 계정 삭제] 사용자 ID: {}, url: {}", userDetails.getUsername(),
				this.request.getRequestURL().toString());
			return true;
		} catch (RuntimeException re) {
			throw new ServiceException(ResponseCodeType.SERVER_ERROR_42001005, re);
		}
	}

	/**
	 * 계정 중복 조회
	 *
	 * @param userDetails
	 * @param username
	 * @return
	 */
	@Transactional
	public boolean checkDuplicate(UserDetails userDetails, String username) {
		log.info("[REQ 계정 중복 조회] 사용자 ID: {}, url: {}", userDetails.getUsername(),
			this.request.getRequestURL().toString());
		boolean tf = this.accountRepository.findByUsername(username).isPresent();
		log.info("[RES 계정 중복 조회] 사용자 ID: {}, url: {}", userDetails.getUsername(),
			this.request.getRequestURL().toString());
		return tf;
	}

	/**
	 * 계정 전체 조회
	 *
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AccountDetail> findAll() {
		return this.accountRepository.findAll().stream()
			.map(a -> {
				AccountDetail accountDetail = this.accountMapper.toDto(a);
				accountDetail.setPassword("");
				accountDetail.convertXss();

				return accountDetail;
			})
			.collect(Collectors.toList());
	}

	/**
	 * 계정 차단
	 *
	 * @return
	 */
	@Transactional
	public boolean blockAccounts(UserDetails userDetails, List<Long> ids) {
		try {
			log.info("[REQ 계정 차단] 사용자 ID: {}, url: {}", userDetails.getUsername(),
				this.request.getRequestURL().toString());

			ids.forEach(id -> accountRepository.getOne(id).setStatus(AccountStatusType.BLOCK.code()));

			log.info("[RES 계정 차단] 사용자 ID: {}, url: {}", userDetails.getUsername(),
				this.request.getRequestURL().toString());
			return true;
		} catch (RuntimeException re) {
			throw new ServiceException(ResponseCodeType.SERVER_ERROR_42001004, re);
		}
	}

	/**
	 * 계정 승인
	 *
	 * @return
	 */
	@Transactional
	public boolean permitAccounts(UserDetails userDetails, List<Long> ids) {
		try {
			log.info("[REQ 계정 승인] 사용자 ID: {}, url: {}", userDetails.getUsername(),
				this.request.getRequestURL().toString());

			ids.forEach(id -> accountRepository.findById(id).ifPresent(a -> {
				if (AccountStatusType.LOCKED.code().equals(a.getStatus())) {
					a.setAccountLockedDt(null);
				}

				a.setStatus(AccountStatusType.APPROVAL.code());
				a.setUpdId(userDetails.getUsername());
				a.setUpdDt(LocalDateTime.now());

				this.loginHistoryRepository.findTop1ByAccountIdOrderByHistorySeqDesc(a.getAccountId()).ifPresent(h -> {
					h.setAuthToken("12345678-9012-3456-7890-123456789012");
					h.setOtp(null);
					h.setLoginFailCnt(0);
					h.setOtpAuthTryCnt(0);
					h.setOtpAuthFailCnt(0);
				});
			}));

			log.info("[RES 계정 승인] 사용자ID: {}, url: {}", userDetails.getUsername(),
				this.request.getRequestURL().toString());
			return true;
		} catch (RuntimeException re) {
			throw new ServiceException(ResponseCodeType.SERVER_ERROR_42001004, re);
		}
	}

}
