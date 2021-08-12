package my.springcloud.account.config;

import my.springcloud.common.constants.CommonConstants;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.sec.model.CustomUserDetails;
import my.springcloud.common.sec.model.CustomUserDetailsHelper;
import my.springcloud.account.exception.AdminAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("GET".equals(request.getMethod())) {
            log.debug("> [{}] url: {} status: PASS", request.getMethod(), request.getRequestURL());
            return true;
        }

        String jwtToken = request.getHeader(CommonConstants.TOKEN_HEADER);
        CustomUserDetails admin = this.customUserDetailsHelper.getSimpleUser(jwtToken);
        String tempToken = request.getHeader(CommonConstants.RECONFIRM_TOKEN_HEADER);

        return checkTempToken(admin.getAccountId(), tempToken);
    }

    private boolean checkTempToken(Long accountId, String tempToken) {
        try {
            boolean tf = this.passwordEncoder.matches(String.valueOf(accountId), tempToken);
            log.debug("> temp token 확인: accountId: {}, tempToken: {}, 매칭여부: {}", accountId, tempToken, tf);

            if (tf) {
                return true;
            }
            throw new AdminAuthException(ResponseCodeType.SERVER_ERROR_41001002);
        }
        catch (IllegalArgumentException e) {
            throw new AdminAuthException(ResponseCodeType.SERVER_ERROR_41001002);
        }
        catch (Exception e) {
            log.error("> Invalid tempToken! token: {}, accountId: {}", tempToken, accountId, e);
            throw new AdminAuthException(ResponseCodeType.SERVER_ERROR_41001002);
        }
    }

}
