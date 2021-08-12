package my.springcloud.common.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@Schema(name = "TokenDto", description = "토큰 DTO")
public class TokenDto implements Serializable {

    @Schema(name = "accessToken", description = "액세스 토큰")
    private final String accessToken;

    @Schema(name = "refreshToken", description = "리프레시 토큰")
    private final String refreshToken;

}
