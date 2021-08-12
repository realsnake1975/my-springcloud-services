package my.springcloud.account.service;

import my.springcloud.account.model.aggregate.Account;
import my.springcloud.common.model.dto.auth.AuthCheckDto;
import my.springcloud.common.sec.model.CustomUserDetails;
import my.springcloud.account.exception.AdminAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {

    private final AuthService authService;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String loginInfo) {
        log.debug("> loginInfo: {}", loginInfo);
        String[] infos = loginInfo.split(",", -1);

        return new CustomUserDetails(
                infos[0]
                , infos[1]
                , AuthorityUtils.createAuthorityList("ROLE_" + infos[2])
                , Long.parseLong(infos[3])
        );
    }

    @Transactional(noRollbackFor = { AdminAuthException.class })
    public UserDetails checkLoginHistoryAndFindUser(AuthCheckDto authCheck) {
        Account account = this.authService.checkLoginHistory(authCheck.getAuthToken(), authCheck.getOtp());
        String loginInfo = account.getUsername() + "," + account.getAccountName() + "," + account.getAuthority().getAuthorityId() + "," + account.getAccountId();
        return this.loadUserByUsername(loginInfo);
    }

}
