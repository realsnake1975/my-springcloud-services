package my.springcloud.account.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * 웹 설정
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ReconfirmInterceptor reconfirmInterceptor;

    private static final List<String> URL_PATTERNS = Arrays.asList(
            "/svc/v1/tutorials/**",
            "/brdprd/v1/shoppingchannel/**",
            "/orgn/v1/appmenus/**",
            "/orgn/v1/containers/**");

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.reconfirmInterceptor)
                .addPathPatterns(URL_PATTERNS);
    }

}