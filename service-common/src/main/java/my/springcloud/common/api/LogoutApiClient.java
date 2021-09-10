package my.springcloud.common.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import my.springcloud.common.wrapper.CommonModel;
import my.springcloud.config.openfeign.AuthRequestInterceptor;

@FeignClient(name = "logoutApiClient", url = "${my-springcloud.services.uri-root.account}", configuration = {AuthRequestInterceptor.class})
public interface LogoutApiClient {

	/**
	 * 로그아웃
	 */
	@PostMapping("/v1/logout")
	CommonModel<Boolean> logout();

}
