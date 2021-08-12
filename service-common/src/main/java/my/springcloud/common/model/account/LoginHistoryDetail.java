package my.springcloud.common.model.account;

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
@ToString(exclude = {
		"historySeq"
})
@Schema(name = "LoginHistoryDto", description = "로그인 이력 DTO")
public class LoginHistoryDetail implements Serializable {

	private static final long serialVersionUID = -6605465516954409942L;

	@Schema(name = "historySeq", description = "아이디")
	private Long historySeq;

	@Schema(name = "accountId", description = "계정아이디")
	private Long accountId;

	@Schema(name = "loginReqDt", description = "로그인요청일시")
	private LocalDateTime loginReqDt;

	@Schema(name = "authToken", description = "인증토큰")
	private String authToken;

	@Schema(name = "otp", description = "One time password")
	private String otp;

	@Schema(name = "loginDt", description = "로그인일시")
	private LocalDateTime loginDt;

}
