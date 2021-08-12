package my.springcloud.common.sec.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.springcloud.common.constants.CommonConstants;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.sec.model.CustomUserDetails;
import my.springcloud.common.sec.model.CustomUserDetailsHelper;
import my.springcloud.common.wrapper.CommonModel;
import my.springcloud.config.jackson.JacksonMapperConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 인증(로그인) 필터
 */
@Slf4j
@Deprecated
public class ServiceAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsHelper customUserDetailsHelper;
    private final ObjectMapper objectMapper;

    public ServiceAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setFilterProcessesUrl(CommonConstants.AUTH_LOGIN_URL);

        this.authenticationManager = authenticationManager;
        this.objectMapper = new JacksonMapperConfig().objectMapper();
        this.customUserDetailsHelper = new CustomUserDetailsHelper();
    }

    @SuppressWarnings("rawtypes")
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        log.debug("> 로그인 시도: {}", username);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // CustomAuthenticationProvider 호출
        return this.authenticationManager.authenticate(authenticationToken);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws IOException {
		// loadUserByUsername이 반환한 UserDetails(CustomUserDetails)
    	CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();
        // log.debug("> 로그인 성공, JWT 반환, ldapId: {}, name: {}, roles: {}", customUser.getUsername(), customUser.getName(), customUser.getAuthorities());

        // JWT 생성
        String accessToken = this.customUserDetailsHelper.generateAccessToken(customUser);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(this.objectMapper.writeValueAsString(new CommonModel<>(accessToken)));
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.info("> 로그인 실패: {}", failed.toString());

        SecurityContextHolder.clearContext();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(this.objectMapper.writeValueAsString(new CommonModel<>(ResponseCodeType.SERVER_ERROR_41001001)));
        response.getWriter().flush();
    }

}
