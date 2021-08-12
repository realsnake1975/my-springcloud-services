package my.springcloud.common.model.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = {
		"historySeq"
})
@Schema(name = "LoginHistoryDto", description = "로그인 이력 DTO")
@ToString(exclude = {
		"historySeq"
})
public class LoginHistoryDto implements Serializable {

	private static final long serialVersionUID = -6605465516954409942L;

	@Schema(name = "historySeq", description = "아이디")
	private Long historySeq;

	// 계정아이디
	@Schema(name = "accountId", description = "계정아이디")
	private Long accountId;

	// 로그인요청일시
	@Schema(name = "loginReqDt", description = "로그인요청일시")
	private LocalDateTime loginReqDt;

	// 인증토큰
	@Schema(name = "authToken", description = "인증토큰")
	private String authToken;

	// OTP
	@Schema(name = "otp", description = "One time password")
	private String otp;

	// 로그인일시
	@Schema(name = "loginDt", description = "로그인일시")
	private LocalDateTime loginDt;

}
