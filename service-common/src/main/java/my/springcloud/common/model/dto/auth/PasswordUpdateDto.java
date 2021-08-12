package my.springcloud.common.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Schema(name = "PasswordUpdateDto", description = "비밀번호 변경 DTO")
public class PasswordUpdateDto implements Serializable {

    // 현재 비밀번호
    @Schema(name = "nowPassword", description = "현재 비밀번호")
    private String nowPassword;

    // 신규 비밀번호
    @Schema(name = "newPassword", description = "신규 비밀번호")
    private String newPassword;

}
