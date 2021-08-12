package my.springcloud.account.service;

import my.springcloud.account.domain.spec.AccountSpec;
import my.springcloud.account.domain.repository.LoginHistoryRepository;
import my.springcloud.common.constants.AccountStatusType;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.exception.ResourceNotFoundException;
import my.springcloud.common.exception.ServiceException;
import my.springcloud.common.model.account.AccountDetail;
import my.springcloud.common.model.account.AccountModify;
import my.springcloud.common.sec.model.CustomUserDetails;
import my.springcloud.account.domain.aggregate.Account;
import my.springcloud.account.mapper.AccountMapper;
import my.springcloud.account.domain.repository.AccountRepository;
import my.springcloud.account.domain.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    private final AuthorityRepository authorityRepository;
    private final LoginHistoryRepository loginHistoryRepository;

    private final HttpServletRequest request;

    private final String TB_ACCOUNT_PK = "accountId";

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
        log.info("[REQ 계정 단건 조회] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
        log.debug("> 계정 단건 조회 id: {}", id);

        Account account = this.findById(id);
        AccountDetail accountDetail = this.accountMapper.toDto(account);
        accountDetail.setPassword("");
        accountDetail.convertXss();

        log.info("[RES 계정 단건 조회] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
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
        log.info("[REQ 계정 목록 조회] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());

        Page<Account> page = this.accountRepository.findAll(spec, pageable);
        Page<AccountDetail> accountDtoPage = page.map(this.accountMapper::toDto);
        accountDtoPage.getContent().forEach(accountDto -> accountDto.setPassword(""));
        log.info("[RES 계정 목록 조회] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());

        return accountDtoPage.map(accountDto -> {
                    accountDto.convertXss();
                    return accountDto;
                });
    }

    /**
     * 계정 등록
     *
     * @param userDetails
     * @param accountCreateDto
     * @return
     */
    @Transactional
    public AccountDetail create(@AuthenticationPrincipal UserDetails userDetails, AccountDetail accountCreateDto) {
        try {
            log.info("[REQ 계정 등록] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
            log.debug("> 계정 등록 accountCreateDto: {}", accountCreateDto.toString());
            CustomUserDetails loginUser = (CustomUserDetails) userDetails;
//        log.debug("> 인증(권한) 확인, username: {}, name: {}, roles: {}", loginUser.getUsername(), loginUser.getName(), loginUser.getAuthorities());

            if (!this.checkPasswordPattern(accountCreateDto.getPassword())) {
                throw new ServiceException(ResponseCodeType.SERVER_ERROR_41001014);
            }

            accountCreateDto.setRegId(loginUser.getUsername());
            accountCreateDto.setUpdId(loginUser.getUsername());

            String encodedPassword = passwordEncoder.encode(accountCreateDto.getPassword());
            accountCreateDto.setPassword(encodedPassword);

            AccountDetail returnDto = accountMapper.toDto(accountRepository.save(accountMapper.toEntity(accountCreateDto)));
            returnDto.setPassword("");

            returnDto.convertXss();
            return returnDto;
        } catch (ServiceException se) {
            log.error("> {}", se.getResponseCodeType().desc());
            throw new ServiceException(se.getResponseCodeType(), se);
        } finally {
            log.info("[RES 계정 등록] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
        }

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
    public AccountDetail modify(@AuthenticationPrincipal UserDetails userDetails, long id, AccountModify accountModify) {
        try {
            log.info("[REQ 계정 수정] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
            log.debug("> 계정 수정 id: {}, accountModifyDto: {}", id, accountModify);
            CustomUserDetails loginUser = (CustomUserDetails) userDetails;
//        log.debug("> 인증(권한) 확인, username: {}, name: {}, roles: {}", loginUser.getUsername(), loginUser.getName(), loginUser.getAuthorities());

            Account account = accountRepository.getOne(id);

            if (!accountModify.getPassword().equals("")
                    && (this.checkPasswordPattern(accountModify.getPassword()) == false
                    || passwordEncoder.matches(accountModify.getPassword(), account.getPassword()))) {
                throw new ServiceException(ResponseCodeType.SERVER_ERROR_41001014);
            }

            if (!accountMapper.toDto(account).equals(accountModify)) {
                if (!accountModify.getPassword().equals("")) {
                    String encodedPassword = passwordEncoder.encode(accountModify.getPassword());
                    accountModify.setPassword(encodedPassword);
                    account.setPassword(accountModify.getPassword());
                    account.setPasswordUpdDt(LocalDateTime.now());
                }
                account.setAuthority(authorityRepository.getOne(accountModify.getAuthority().getAuthorityId()));
                account.setPhoneNumber(accountModify.getPhoneNumber());
                account.setEmail(accountModify.getEmail());
                account.setUpdDt(LocalDateTime.now());
                account.setUpdId(loginUser.getUsername());
            }
            AccountDetail accountDetail = accountMapper.toDto(account);
            accountDetail.setPassword("");

            accountDetail.convertXss();
            return accountDetail;
        } catch (ServiceException se) {
            log.error("> {}", se.getResponseCodeType().desc());
            throw new ServiceException(se.getResponseCodeType(), se);
        } finally {
            log.info("[RES 계정 수정] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
        }
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
        log.info("[REQ 계정 삭제] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
        log.debug("> 계정 삭제 ids: {}", ids.toString());
//        CustomUserDetails loginUser = (CustomUserDetails) userDetails;
//        log.debug("> 인증(권한) 확인, username: {}, name: {}, roles: {}", loginUser.getUsername(), loginUser.getName(), loginUser.getAuthorities());

        try {
            ids.forEach(accountRepository::deleteById);
            return true;
        } catch (RuntimeException re) {
            throw new ServiceException(ResponseCodeType.SERVER_ERROR_42001005, re);
        } finally {
            log.info("[RES 계정 삭제] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
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
        log.info("[REQ 계정 중복 조회] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
        log.debug("> 계정 중복 조회 username: {}", username);
        log.info("[RES 계정 중복 조회] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
        return accountRepository.findByUsername(username).isPresent();
    }

    /**
     * 계정 전체 조회
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<AccountDetail> findAll() {
        List<AccountDetail> accountDetailList = this.accountRepository.findAll().stream().map(this.accountMapper::toDto).collect(Collectors.toList());
        accountDetailList.stream().forEach(accountDto -> {
            accountDto.setPassword("");
        });
        accountDetailList.forEach(accountDto -> accountDto.convertXss());
        return accountDetailList;
    }

    /**
     * 계정 차단
     *
     * @return
     */
    @Transactional
    public boolean blockAccounts(UserDetails userDetails, List<Long> ids) {
        try {
            log.info("[REQ 계정 차단] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
            log.debug("> 계정 차단 ids: {}", ids.toString());
            ids.forEach(id -> accountRepository.getOne(id).setStatus(AccountStatusType.BLOCK.code()));
            return true;
        } catch (RuntimeException re) {
            log.error("> {}", ResponseCodeType.SERVER_ERROR_42001004.desc());
            throw new ServiceException(ResponseCodeType.SERVER_ERROR_42001004, re);
        } finally {
            log.info("[RES 계정 차단] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
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
            log.info("[REQ 계정 승인] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
            log.debug("> 계정 승인 ids: {}", ids.toString());

            // ids.forEach(id -> accountRepository.getOne(id).setStatus(AccountStatusType.APPROVAL.code()));

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

            return true;
        } catch (RuntimeException re) {
            log.error("> {}", ResponseCodeType.SERVER_ERROR_42001004.desc());
            throw new ServiceException(ResponseCodeType.SERVER_ERROR_42001004, re);
        } finally {
            log.info("[RES 계정 승인] 사용자ID: {}, url: {}", userDetails.getUsername(), this.request.getRequestURL().toString());
        }
    }

    /**
     * 비밀번호 조건 체크
     *
     * @return
     */
    @Transactional(readOnly = true)
    public boolean checkPasswordPattern(String password) {
        // 영문자 + 숫자
        boolean check1 = Pattern.matches("^(?=.*[a-zA-Z])(?=.*[0-9]).{10,20}$", password);
        // 영문 + 특수문자
        boolean check2 = Pattern.matches("^(?=.*[a-zA-Z])(?=.*[!$%^]).{10,20}$", password);
        // 특수문자 + 숫자
        boolean check3 = Pattern.matches("^(?=.*[!$%^])(?=.*[0-9]).{10,20}$", password);
        // 영문자 , 숫자 , 특수문자(!$%^) 중 3가지 조합 ( 8~20 )
        boolean check4 = Pattern.matches("^(?=.*[a-zA-Z])(?=.*[!$%^])(?=.*[0-9]).{8,20}$", password);

        if (!(check1 || check2 || check3 || check4)) {
            log.info("Password condition mismatch!!");
            log.info("영문자 + 숫자 {}, 영문 + 특수문자 {}, 특수문자 + 숫자 {}, 영문자 , 숫자 , 특수문자(!$%^) 중 3가지 조합 {}", check1, check2, check3, check4);
            return false;
        }

        //	연속적인 숫자
        boolean check6 = Pattern.matches("/(00)|(11)|(22)|(33)|(44)|(55)|(66)|(77)|(88)|(99)/", password);

        if (check6) {
            log.info("Password condition mismatch!!");
            return false;
        }

        return true;
    }

}
