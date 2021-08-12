package my.springcloud.common.sec.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.springcloud.common.constants.CommonConstants;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.sec.model.CustomUserDetails;
import my.springcloud.common.sec.model.CustomUserDetailsHelper;
import static my.springcloud.common.utils.TextUtils.isEmpty;
import my.springcloud.common.wrapper.CommonModel;
import my.springcloud.common.wrapper.CustomHttpServletRequestWrapper;
import my.springcloud.config.jackson.JacksonMapperConfig;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ServiceAuthorizationFilter extends BasicAuthenticationFilter {

    private final CustomUserDetailsHelper customUserDetailsHelper;
    private final ObjectMapper objectMapper;

    public ServiceAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);

        this.customUserDetailsHelper = new CustomUserDetailsHelper();
        this.objectMapper = new JacksonMapperConfig().objectMapper();
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        CommonModel commonModel;
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
        }
        catch (ExpiredJwtException e) {
            log.info("> JWT 유효기한 만료: {}", e.getMessage());
            commonModel = new CommonModel<>(ResponseCodeType.SERVER_ERROR_41001002);
        }
        catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            log.info("> JWT 형식 오류: {}", e.getMessage());
            commonModel = new CommonModel<>(ResponseCodeType.SERVER_ERROR_41001001);
        }
        catch (RuntimeException e) {
            log.info("> JWT 인증 실패: {}", e.getMessage());
            commonModel = new CommonModel<>(ResponseCodeType.SERVER_ERROR_41001001);
        }

        SecurityContextHolder.clearContext();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(this.objectMapper.writeValueAsString(commonModel));
        response.getWriter().flush();
    }

    @SuppressWarnings("deprecation")
    private Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(CommonConstants.TOKEN_HEADER);
        log.debug("> token: {}", token);

        if (isEmpty(token) || request.getRequestURI().contains("/svc/api/")) {
            return null;
        }

        CustomUserDetails admin = this.customUserDetailsHelper.getSimpleUser(token);
        log.debug("> 인증 여부 확인, accountId: {}, username: {}, name: {}, roles: {}", admin.getAccountId(), admin.getUsername(), admin.getName(), admin.getAuthorities());

        if (this.customUserDetailsHelper.isRefreshToken(token)) {
            throw new SignatureException("인증 여부 확인은 accessToken만 가능합니다!");
        }

        return new UsernamePasswordAuthenticationToken(admin, "", admin.getAuthorities());
    }

}
