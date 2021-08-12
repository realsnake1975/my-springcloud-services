package my.springcloud.common.api;

import my.springcloud.common.wrapper.CommonModel;
import my.springcloud.config.openfeign.AuthRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "logoutApiClient", url = "${my-springcloud.services.uri-root.account}", configuration = { AuthRequestInterceptor.class })
public interface LogoutApiClient {

    /**
     * 로그아웃
     */
    @PostMapping("/opr/v1/logout")
    CommonModel<Boolean> logout();

}
