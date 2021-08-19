package my.springcloud.account.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.common.model.auth.AuthCheck;
import my.springcloud.common.sec.model.CustomUserDetails;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	private final AuthService authService;
	private final AdminDetailsService adminDetailsService;

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
		final AuthCheck authCheck = (AuthCheck)authentication.getPrincipal();
		return this.adminDetailsService.checkLoginHistoryAndFindUser(authCheck);
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
		UsernamePasswordAuthenticationToken authentication) {
		final CustomUserDetails admin = (CustomUserDetails)userDetails;
		boolean nonExpiredPasswordYn = this.authService.updateLoginDtAndCheckNonExpiredPassword(admin.getAccountId());
		admin.setNonExpiredPasswordYn(nonExpiredPasswordYn);
	}

}
