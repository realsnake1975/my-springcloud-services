package my.springcloud.account.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import my.springcloud.common.model.AttachFileDetail;
import my.springcloud.common.model.account.AccountCreate;
import my.springcloud.common.model.account.AccountDetail;
import my.springcloud.common.model.account.AccountModify;
import my.springcloud.common.sec.model.CustomUserDetails;
import my.springcloud.common.utils.TextUtils;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AccountService {

	private final AccountRepository accountRepository;
	private final AuthorityRepository authorityRepository;
	private final LoginHistoryRepository loginHistoryRepository;

	private final AttachFileComponent attachFileComponent;

	private final PasswordEncoder passwordEncoder;
	private final AccountMapper accountMapper;
	private final ObjectMapper objectMapper;

	private Account findById(long id) {
		return this.accountRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
	}

	/**
	 * 계정 단건 조회
	 *
	 * @param id 계정 아이디
	 * @return 계정 상세
	 */
	@Transactional(readOnly = true)
	public AccountDetail find(long id) {
		Account account = this.findById(id);
		AccountDetail accountDetail = this.accountMapper.toDto(account);
		return accountDetail.convertXssAndPasswordEmpty();
	}

	/**
	 * 계정 목록 조회
	 *
	 * @param spec 검색 조건
	 * @param pageable 페이징
	 * @return 계정 목록 페이징
	 */
	@Transactional(readOnly = true)
	public Page<AccountDetail> find(AccountSpec spec, Pageable pageable) {
		return this.accountRepository.findAll(spec, pageable).map(a -> {
			AccountDetail accountDetail = this.accountMapper.toDto(a);
			return accountDetail.convertXssAndPasswordEmpty();
		});
	}

	/**
	 * 계정 등록
	 *
	 * @param userDetails 로그인 사용자
	 * @param accountCreate 등록할 계정
	 * @return 계정 상세
	 */
	public AccountDetail create(UserDetails userDetails, AccountCreate accountCreate) {
		PasswordValidator.validatePassword(accountCreate.getPassword());

		CustomUserDetails loginUser = (CustomUserDetails) userDetails;
		accountCreate.setRegId(loginUser.getUsername());

		String encodedPassword = this.passwordEncoder.encode(accountCreate.getPassword());
		accountCreate.setPassword(encodedPassword);

		Account account = this.accountMapper.toEntity(accountCreate);
		account.setAuthority(this.authorityRepository.findById(accountCreate.getAuthorityId()).orElseThrow(() -> new ServiceException(ResponseCodeType.SERVER_ERROR_41001015)));

		AccountDetail accountDetail = this.accountMapper.toDto(this.accountRepository.save(account.publish("reg")));
		return accountDetail.convertXssAndPasswordEmpty();
	}

	/**
	 * 계정 수정
	 *
	 * @param userDetails 로그인 사용자
	 * @param id 수정할 계정 아이디
	 * @param accountModify 수정할 계정
	 * @return 계정 상세
	 */
	public AccountDetail modify(UserDetails userDetails, long id, AccountModify accountModify) {
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

			account.setAuthority(this.authorityRepository.findById(accountModify.getAuthorityId()).orElseThrow(() -> new ServiceException(ResponseCodeType.SERVER_ERROR_41001015)));
			account.setPhoneNumber(accountModify.getPhoneNumber());
			account.setEmail(accountModify.getEmail());
			account.setUpdId(loginUser.getUsername());
		}

		AccountDetail accountDetail = this.accountMapper.toDto(account);
		return accountDetail.convertXssAndPasswordEmpty();
	}

	/**
	 * 계정 삭제
	 *
	 * @param userDetails 로그인 사용자
	 * @param ids 계정 아이디들
	 * @return 삭제된 계정 일련번호들
	 */
	public List<Long> remove(UserDetails userDetails, List<Long> ids) {
		// TODO: 로그인 사용자 권한 체크

		List<Long> deletedIds = new ArrayList<>();

		ids.forEach(id -> {
			if (this.accountRepository.existsById(id)) {
				this.accountRepository.deleteById(id);
				deletedIds.add(id);
			}
		});

		return deletedIds;
	}

	/**
	 * 계정 중복 조회
	 *
	 * @param username 로그인 아이디
	 * @return true or false
	 */
	@Transactional(readOnly = true)
	public boolean checkDuplicate(String username) {
		return this.accountRepository.findByUsername(username).isPresent();
	}

	/**
	 * 계정 전체 조회
	 *
	 * @return 계정 목록
	 */
	@Transactional(readOnly = true)
	public List<AccountDetail> findAll() {
		return this.accountRepository.findAll().stream()
			.map(a -> {
				AccountDetail accountDetail = this.accountMapper.toDto(a);
				return accountDetail.convertXssAndPasswordEmpty();
			})
			.collect(Collectors.toList());
	}

	/**
	 * 계정 차단
	 *
	 * @param userDetails 로그인 사용자
	 * @param ids 계정 아이디들
	 * @return 차단된 계정 일련번호들
	 */
	public List<Long> blockAccounts(UserDetails userDetails, List<Long> ids) {
		List<Long> blockedIds = new ArrayList<>();

		ids.forEach(id -> this.accountRepository.findById(id).ifPresent(a -> {
			a.setStatus(AccountStatusType.BLOCK.code());
			a.setUpdId(userDetails.getUsername());
			a.setUpdDt(LocalDateTime.now());

			blockedIds.add(id);
		}));

		return blockedIds;
	}

	/**
	 * 계정 승인
	 *
	 * @param userDetails 로그인 사용자
	 * @param ids 계정 아이디들
	 * @return 승인된 계정 일련번호들
	 */
	public List<Long> approveAccounts(UserDetails userDetails, List<Long> ids) {
		List<Long> approvedIds = new ArrayList<>();

		ids.forEach(id -> this.accountRepository.findById(id).ifPresent(a -> {
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

			approvedIds.add(id);
		}));

		return approvedIds;
	}

	/**
	 * 계정 등록 및 첨부파일 저장
	 *
	 * @param userDetails 로그인 사용자
	 * @param accountCreateJsonString JSON 문자열 형태의 등록할 계정
	 * @param multipartFiles 첨부파일들
	 * @return 계정 상세
	 */
	@SneakyThrows
	public AccountDetail createAndSave(UserDetails userDetails, String accountCreateJsonString, MultipartFile[] multipartFiles) {
		AccountCreate accountCreate = this.objectMapper.readValue(accountCreateJsonString, AccountCreate.class);
		AccountDetail accountDetail = this.create(userDetails, accountCreate);

		// final long parentId = accountDetail.getAccountId();
		List<AttachFileDetail> attachFiles = this.attachFileComponent.save(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM")), multipartFiles);
		// TODO: 첨부파일 데이터 DB 저장

		return accountDetail;
	}

}
