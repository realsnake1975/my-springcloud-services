package my.springcloud.account.config;

import my.springcloud.account.service.AdminAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * 스프링 스큐리티 설정
 */
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AdminAuthenticationProvider adminAuthenticationProvider;
    private final AdminAccessDecisionVoter adminAccessDecisionVoter;

    @Value("${jwt.exp:3}")
    private long jwtExp;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
	        .and()
	        .csrf().disable()
	        .authorizeRequests()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
	        .antMatchers("/opr/v1/auth/**", "/opr/v1/codes/**", "/health-hssvc").permitAll()
	        .anyRequest().authenticated()
            .accessDecisionManager(this.accessDecisionManager())
	        .and()
	        .addFilter(new AdminAuthenticationFilter(this.authenticationManager(), this.jwtExp))
	        .addFilter(new AuthorizationFilter(this.authenticationManager()))
	        .sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .logout()
            .logoutUrl("/opr/v1/logout")
            .logoutSuccessHandler(this.logoutSuccessHandler())
	        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.adminAuthenticationProvider);
    }

    @SuppressWarnings("unchecked")
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new UnanimousBased(Arrays.asList(new WebExpressionVoter(), new RoleVoter(), new AuthenticatedVoter(), this.adminAccessDecisionVoter));
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new AdminLogoutSuccessHandler();
    }

    // https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/cors.html
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        // configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList(
                HttpMethod.HEAD.name(),
                HttpMethod.OPTIONS.name(),
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.PUT.name()
                )
        );
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(false);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 스프링 시큐리티 필터 무시
     *
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/actuator/**"
                , "/opr/swagger-ui.html"
                , "/opr/swagger*/**"
                , "/webjars/**"
                , "/h2-console*/**"
                , "/favicon.ico"
                , "/v3/api-docs/**"
        );
    }

}
