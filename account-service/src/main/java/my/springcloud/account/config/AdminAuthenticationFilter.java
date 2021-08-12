package my.springcloud.account.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.model.auth.AuthCheck;
import my.springcloud.common.model.auth.TokenDetail;
import my.springcloud.common.sec.model.CustomUserDetails;
import my.springcloud.common.sec.model.CustomUserDetailsHelper;
import my.springcloud.common.wrapper.CommonModel;
import my.springcloud.config.jackson.JacksonMapperConfig;
import my.springcloud.account.exception.AdminAuthException;
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
public class AdminAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final CustomUserDetailsHelper customUserDetailsHelper;

    public AdminAuthenticationFilter(AuthenticationManager authenticationManager, long jwtExp) {
        super.setFilterProcessesUrl("/opr/v1/auth/final");

        this.authenticationManager = authenticationManager;
        this.objectMapper = new JacksonMapperConfig().objectMapper();
        this.customUserDetailsHelper = new CustomUserDetailsHelper(jwtExp);
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        AuthCheck authCheck = this.objectMapper.readValue(request.getInputStream(), AuthCheck.class);
		log.debug("> 로그인 시도: {}", authCheck.toString());

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authCheck, "");

        // AdminAuthenticationProvider 호출
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws IOException {
		// loadUserByUsername이 반환한 UserDetails(AdminUserDetails)
    	CustomUserDetails admin = (CustomUserDetails) authentication.getPrincipal();
        log.debug("> 인증 성공, accountId: {}, username: {}, name: {}, roles: {}", admin.getAccountId(), admin.getUsername(), admin.getName(), admin.getAuthorities());

        // JWT 생성
        String accessToken = this.customUserDetailsHelper.generateAccessToken(admin);
        String refreshToken = this.customUserDetailsHelper.generateRefreshToken(admin);

        ResponseCodeType responseCodeType;
        if (admin.isNonExpiredPasswordYn()) {
            responseCodeType = ResponseCodeType.SUCCESS;
        }
        else {
            responseCodeType = ResponseCodeType.SERVER_ERROR_41001005;
        }

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(this.objectMapper.writeValueAsString(new CommonModel<>(responseCodeType, new TokenDetail(accessToken, refreshToken))));
        response.flushBuffer();
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.info("> 인증 실패: {}", failed.toString());

        SecurityContextHolder.clearContext();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        CommonModel cm;
        if (failed instanceof AdminAuthException) {
            ResponseCodeType responseCodeType = ((AdminAuthException) failed).getResponseCodeType();
            cm = new CommonModel<>(responseCodeType);
        }
        else {
            cm = new CommonModel<>(ResponseCodeType.SERVER_ERROR_41001001);
        }

		response.getWriter().write(this.objectMapper.writeValueAsString(cm));
        response.flushBuffer();
    }

}
