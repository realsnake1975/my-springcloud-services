package my.springcloud.common.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Schema(name = "LoginDto", description = "로그인 DTO")
public class LoginDto implements Serializable {

    // 로그인 아이디
    @Schema(name = "username", description = "로그인 아이디")
    private String username;

    // 비밀번호
    @Schema(name = "password", description = "비밀번호")
    private String password;

}
