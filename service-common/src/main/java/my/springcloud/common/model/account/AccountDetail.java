package my.springcloud.common.model.account;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import my.springcloud.common.utils.TextUtils;

@Getter
@Setter
@EqualsAndHashCode(exclude = {
	"accountId"
	, "username"
	, "loginHistories"
	, "accountName"
	, "companyName"
	, "status"
	, "regId"
	, "regDt"
	, "updId"
	, "updDt"
})
@ToString(exclude = {
	"accountId"
})
@Schema(name = "AccountDto", description = "계정 DTO")
public class AccountDetail implements Serializable {

	private static final long serialVersionUID = -6605465516954409942L;

	@Schema(name = "accountId", description = "계정아이디")
	private long accountId;

	@Schema(name = "username", description = "계정아이디")
	private String username;

	@Schema(name = "authority", description = "권한아이디")
	private AuthorityDetail authority;

	@Schema(name = "loginHistories", description = "로그인 이력")
	private List<LoginHistoryDetail> loginHistories = new ArrayList<>();

	@Schema(name = "password", description = "비밀번호")
	private String password;

	@Schema(name = "accountName", description = "이름")
	private String accountName;

	@Schema(name = "companyName", description = "소속회사명")
	private String companyName;

	@Schema(name = "phoneNumber", description = "연락처")
	private String phoneNumber;

	@Schema(name = "email", description = "이메일")
	private String email;

	@Schema(name = "regId", description = "등록자아이디")
	private String regId;

	@Schema(name = "regDt", description = "등록일시")
	private LocalDateTime regDt;

	@Schema(name = "status", description = "계정상태")
	private String status;

	@Schema(name = "updId", description = "수정자아이디")
	private String updId;

	@Schema(name = "updDt", description = "수정자아이디")
	private LocalDateTime updDt;

	@Schema(name = "passwordUpdDt", description = "비밀번호 변경일시")
	private LocalDateTime passwordUpdDt;

	@Schema(name = "accountLockedDt", description = "계정 잠금일시")
	private LocalDateTime accountLockedDt;

	public void convertXss() {
		if (username != null) {
			username = TextUtils.convertXss(username);
		}
		if (authority != null) {
			authority.convertXss();
		}
		if (accountName != null) {
			accountName = TextUtils.convertXss(accountName);
		}
		if (phoneNumber != null) {
			phoneNumber = TextUtils.convertXss(phoneNumber);
		}
		if (email != null) {
			email = TextUtils.convertXss(email);
		}
	}

}
