package my.springcloud.common.model.auth;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "PasswordUpdate", description = "비밀번호 변경 DTO")
public class PasswordUpdate implements Serializable {

	@Schema(name = "nowPassword", description = "현재 비밀번호")
	private String nowPassword;

	@Schema(name = "newPassword", description = "신규 비밀번호")
	private String newPassword;

}
