package my.springcloud.common.model.auth;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "AuthCheckDto", description = "인증왁인 DTO")
public class AuthCheck implements Serializable {

	@Schema(name = "authToken", description = "인증토큰")
	private String authToken;

	@Schema(name = "otp", description = "6자리 One time password")
	private String otp;

}
