package my.springcloud.common.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(value = {
	"newPassword"
})
@Schema(name = "PasswordCheck", description = "비밀번호 확인 DTO")
public class PasswordCheck extends PasswordUpdate {
}
