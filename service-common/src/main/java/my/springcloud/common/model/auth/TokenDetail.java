package my.springcloud.common.model.auth;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(name = "TokenDetail", description = "토큰 DTO")
public class TokenDetail implements Serializable {

	@Schema(name = "accessToken", description = "액세스 토큰")
	private final String accessToken;

	@Schema(name = "refreshToken", description = "리프레시 토큰")
	private final String refreshToken;

}
