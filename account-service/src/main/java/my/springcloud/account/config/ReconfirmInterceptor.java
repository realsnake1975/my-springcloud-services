package my.springcloud.account.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.exception.AdminAuthException;
import my.springcloud.common.constants.CommonConstants;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.sec.model.CustomUserDetails;
import my.springcloud.common.sec.model.CustomUserDetailsHelper;

/**
 *  검수 확인시 Header에서 Reconfirm Token을 체크하는 인터셉터
 */
@Slf4j
@Component
public class ReconfirmInterceptor implements HandlerInterceptor {

	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsHelper customUserDetailsHelper;

	public ReconfirmInterceptor(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
		this.customUserDetailsHelper = new CustomUserDetailsHelper();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if ("GET".equals(request.getMethod())) {
			log.debug("> [{}] url: {} status: PASS", request.getMethod(), request.getRequestURL());
			return true;
		}

		String jwtToken = request.getHeader(CommonConstants.TOKEN_HEADER);
		CustomUserDetails admin = this.customUserDetailsHelper.getSimpleUser(jwtToken);
		String tempToken = request.getHeader(CommonConstants.RECONFIRM_TOKEN_HEADER);

		return checkReconfirmToken(admin.getAccountId(), tempToken);
	}

	private boolean checkReconfirmToken(Long accountId, String tempToken) {
		try {
			return this.passwordEncoder.matches(String.valueOf(accountId), tempToken);
		} catch (Exception e) {
			log.error("Invalid reconfirmToken! token: {}, accountId: {}", tempToken, accountId, e);
			throw new AdminAuthException(ResponseCodeType.SERVER_ERROR_41001002);
		}
	}

}
