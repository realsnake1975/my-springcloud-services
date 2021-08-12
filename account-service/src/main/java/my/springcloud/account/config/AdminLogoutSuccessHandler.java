package my.springcloud.account.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.wrapper.CommonModel;
import my.springcloud.config.jackson.JacksonMapperConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AdminLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ObjectMapper objectMapper;

    public AdminLogoutSuccessHandler() {
        this.objectMapper = new JacksonMapperConfig().objectMapper();
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String refererUrl = request.getHeader("Referer");
        log.debug("> 로그아웃: {}", refererUrl);

        SecurityContextHolder.clearContext();

        // super.onLogoutSuccess(request, response, authentication); // 스프링시큐리티의 기본 로그아웃 후 URL로 이동한다.

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(this.objectMapper.writeValueAsString(new CommonModel<>(ResponseCodeType.SUCCESS, true)));
        response.flushBuffer();
    }

}
