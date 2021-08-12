package my.springcloud.common.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@Schema(name = "AuthCheckDto", description = "인증왁인 DTO")
@ToString
public class AuthCheckDto implements Serializable {

    // 인증토큰
    @Schema(name = "authToken", description = "인증토큰")
    private String authToken;

    // OTP
    @Schema(name = "otp", description = "6자리 One time password")
    private String otp;

}
