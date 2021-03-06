package my.springcloud.common.model.auth;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "LoginCheck", description = "로그인 DTO")
public class LoginCheck implements Serializable {

	@Schema(name = "username", description = "로그인 아이디")
	private String username;

	@Schema(name = "password", description = "비밀번호")
	private String password;

}
