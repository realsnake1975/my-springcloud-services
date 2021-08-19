package my.springcloud.common.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import my.springcloud.common.model.auth.AuthCheck;
import my.springcloud.common.model.auth.LoginCheck;
import my.springcloud.common.model.auth.TokenDetail;
import my.springcloud.common.wrapper.CommonModel;

@FeignClient(name = "authApiClient", url = "${my-springcloud.services.uri-root.account}")
public interface AuthApiClient {

	/**
	 * 로그인
	 *
	 * @param dto - 로그인 DTO
	 * @return 인증토큰(authToken)
	 * @Deprecated
	 */
	@PostMapping("/opr/v1/auth/login")
	CommonModel<String> login(@RequestBody LoginCheck dto);

	/**
	 * SMS 인증번호 요청
	 *
	 * @param authToken - 인증토큰
	 * @Deprecated
	 */
	@PostMapping("/opr/v1/auth/request-sms-otp")
	CommonModel<Boolean> requestSmsOtp(@RequestHeader("x-auth-token") String authToken);

	/**
	 * 인증번호 확인(최종 인증)
	 *
	 * @param dto - 인증확인 DTO
	 * @return JWT(accessToken, refreshToken)
	 */
	@PostMapping("/opr/v1/auth/final")
	CommonModel<TokenDetail> authFinal(@RequestBody AuthCheck dto);

}
