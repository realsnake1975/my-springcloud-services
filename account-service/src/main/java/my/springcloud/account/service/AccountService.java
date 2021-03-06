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
import my.springcloud.account.domain.entity.AccountAttachFile;
import my.springcloud.account.domain.repository.AccountAttachFileRepository;
import my.springcloud.account.domain.repository.AccountRepository;
import my.springcloud.account.domain.repository.AuthorityRepository;
import my.springcloud.account.domain.repository.LoginHistoryRepository;
import my.springcloud.account.domain.spec.AccountSpec;
import my.springcloud.account.mapper.AccountAttachFileMapper;
import my.springcloud.account.mapper.AccountMapper;
import my.springcloud.common.constants.AccountStatusType;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.exception.ResourceNotFoundException;
import my.springcloud.common.exception.ServiceException;
import my.springcloud.common.model.AttachFileDetail;
import my.springcloud.common.model.account.AccountAttachFileDetail;
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
	private final AccountAttachFileRepository attachFileRepository;

	private final AttachFileComponent attachFileComponent;

	private final PasswordEncoder passwordEncoder;
	private final AccountMapper accountMapper;
	private final AccountAttachFileMapper attachFileMapper;
	private final ObjectMapper objectMapper;

	private Account findById(long id) {
		return this.accountRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
	}

	/**
	 * ?????? ?????? ??????
	 *
	 * @param id ?????? ?????????
	 * @return ?????? ??????
	 */
	@Transactional(readOnly = true)
	public AccountDetail find(long id) {
		Account account = this.findById(id);
		AccountDetail accountDetail = this.accountMapper.toDto(account);
		return accountDetail.convertXssAndPasswordEmpty();
	}

	/**
	 * ?????? ?????? ??????
	 *
	 * @param spec ?????? ??????
	 * @param pageable ?????????
	 * @return ?????? ?????? ?????????
	 */
	@Transactional(readOnly = true)
	public Page<AccountDetail> find(AccountSpec spec, Pageable pageable) {
		return this.accountRepository.findAll(spec, pageable).map(a -> {
			AccountDetail accountDetail = this.accountMapper.toDto(a);
			return accountDetail.convertXssAndPasswordEmpty();
		});
	}

	/**
	 * ?????? ??????
	 *
	 * @param userDetails ????????? ?????????
	 * @param accountCreate ????????? ??????
	 * @return ?????? ??????
	 */
	private Account createAccount(UserDetails userDetails, AccountCreate accountCreate) {
		PasswordValidator.validatePassword(accountCreate.getPassword());

		CustomUserDetails loginUser = (CustomUserDetails) userDetails;
		accountCreate.setRegId(loginUser.getUsername());

		String encodedPassword = this.passwordEncoder.encode(accountCreate.getPassword());
		accountCreate.setPassword(encodedPassword);

		Account account = this.accountMapper.toEntity(accountCreate);
		account.setAuthority(this.authorityRepository.findById(accountCreate.getAuthorityId()).orElseThrow(() -> new ServiceException(ResponseCodeType.SERVER_ERROR_41001015)));

		return account;
	}

	/**
	 * ?????? ??????
	 *
	 * @param userDetails ????????? ?????????
	 * @param accountCreate ????????? ??????
	 * @return ?????? ??????
	 */
	public AccountDetail create(UserDetails userDetails, AccountCreate accountCreate) {
		Account account = this.createAccount(userDetails, accountCreate);
		this.accountRepository.save(account.publish("reg"));

		return this.accountMapper.toDto(account).convertXssAndPasswordEmpty();
	}

	/**
	 * ?????? ??????
	 *
	 * @param userDetails ????????? ?????????
	 * @param id ????????? ?????? ?????????
	 * @param accountModify ????????? ??????
	 * @return ?????? ??????
	 */
	public AccountDetail modify(UserDetails userDetails, long id, AccountModify accountModify) {
		PasswordValidator.validatePassword(accountModify.getPassword());

		CustomUserDetails loginUser = (CustomUserDetails)userDetails;
		Account account = this.findById(id);

		// ???????????? ?????? ??? ?????? ??????????????? ???????????? ??????
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
	 * ?????? ??????
	 *
	 * @param userDetails ????????? ?????????
	 * @param ids ?????? ????????????
	 * @return ????????? ?????? ???????????????
	 */
	public List<Long> remove(UserDetails userDetails, List<Long> ids) {
		// TODO: ????????? ????????? ?????? ??????

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
	 * ?????? ?????? ??????
	 *
	 * @param username ????????? ?????????
	 * @return true or false
	 */
	@Transactional(readOnly = true)
	public boolean checkDuplicate(String username) {
		return this.accountRepository.findByUsername(username).isPresent();
	}

	/**
	 * ?????? ?????? ??????
	 *
	 * @return ?????? ??????
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
	 * ?????? ??????
	 *
	 * @param userDetails ????????? ?????????
	 * @param ids ?????? ????????????
	 * @return ????????? ?????? ???????????????
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
	 * ?????? ??????
	 *
	 * @param userDetails ????????? ?????????
	 * @param ids ?????? ????????????
	 * @return ????????? ?????? ???????????????
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
	 * ?????? ?????? ??? ???????????? ??????
	 *
	 * @param userDetails ????????? ?????????
	 * @param accountCreateJsonString JSON ????????? ????????? ????????? ??????
	 * @param multipartFiles ???????????????
	 * @return ?????? ??????
	 */
	@SneakyThrows
	public AccountDetail createAndSave(UserDetails userDetails, String accountCreateJsonString, MultipartFile[] multipartFiles) {
		AccountCreate accountCreate = this.objectMapper.readValue(accountCreateJsonString, AccountCreate.class);
		Account account = this.createAccount(userDetails, accountCreate);

		List<AttachFileDetail> attachFiles = this.attachFileComponent.save(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM")), multipartFiles);

		List<AccountAttachFile> accountAttachFiles = attachFiles.stream()
			// .map(a -> new AccountAttachFileDetail(a, account.getAccountId()))
			.map(AccountAttachFileDetail::new)
			.map(this.attachFileMapper::toEntity)
			.collect(Collectors.toList());

		account.setAttachFiles(accountAttachFiles);

		this.accountRepository.save(account.publish("reg"));

		return this.accountMapper.toDto(account).convertXssAndPasswordEmpty();
	}

	/**
	 * ???????????? ?????? ??????
	 *
	 * @param attachFileId ???????????? ?????????
	 * @return AccountAttachFileDetail
	 */
	@Transactional(readOnly = true)
	public AccountAttachFileDetail getAttachFile(String attachFileId) {
		AccountAttachFile attachFile = this.attachFileRepository.findById(attachFileId).orElseThrow(ResourceNotFoundException::new);
		AccountAttachFileDetail aafd = this.attachFileMapper.toDto(attachFile);
		aafd.setResource(this.attachFileComponent.load(attachFile.getPath(), attachFile.getUpdatedName()));
		return aafd;
	}

}
