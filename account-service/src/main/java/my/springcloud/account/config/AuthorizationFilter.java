package my.springcloud.account.config;

import static my.springcloud.common.utils.TextUtils.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.exception.FilterExceptionHandler;
import my.springcloud.common.constants.CommonConstants;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.sec.model.CustomUserDetails;
import my.springcloud.common.sec.model.CustomUserDetailsHelper;
import my.springcloud.common.wrapper.CustomHttpServletRequestWrapper;

@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {

	private final CustomUserDetailsHelper customUserDetailsHelper;

	public AuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		this.customUserDetailsHelper = new CustomUserDetailsHelper();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		ResponseCodeType responseCodeType;
		try {
			if (request.getRequestURI().contains("/auth/login")) {
				request = new CustomHttpServletRequestWrapper(request);
			}

			Authentication authentication = this.getAuthentication(request);
			if (authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

			filterChain.doFilter(request, response);
			return;
		} catch (ExpiredJwtException e) {
			log.error("JWT ???????????? ??????: {}", e.getMessage());
			responseCodeType = ResponseCodeType.SERVER_ERROR_41001016;
		} catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException | DecodingException e) {
			log.error("JWT ?????? ??????: {}", e.getMessage());
			responseCodeType = ResponseCodeType.SERVER_ERROR_41001017;
		}

		SecurityContextHolder.clearContext();
		FilterExceptionHandler filterExceptionHandler = new FilterExceptionHandler(request);
		filterExceptionHandler.onAuthFail(response, responseCodeType);
	}

	private Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(CommonConstants.TOKEN_HEADER);
		log.debug("> token: {}", token);

		if (isEmpty(token) || request.getRequestURI().contains("/svc/api/")) {
			return null;
		}

		CustomUserDetails admin = this.customUserDetailsHelper.getSimpleUser(token);
		log.debug("> ?????? ?????? ??????, accountId: {}, username: {}, name: {}, roles: {}", admin.getAccountId(), admin.getUsername(), admin.getName(), admin.getAuthorities());

		if (this.customUserDetailsHelper.isRefreshToken(token)) {
			throw new SignatureException("?????? ?????? ????????? accessToken??? ???????????????!");
		}

		return new UsernamePasswordAuthenticationToken(admin, "", admin.getAuthorities());
	}

}
