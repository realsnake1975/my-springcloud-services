package my.springcloud.common.model.dto.account;

import my.springcloud.common.utils.TextUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = {
		"accountId"
		, "username"
		, "loginHistoryList"
		, "accountName"
		, "companyName"
		, "status"
		, "regId"
		, "regDt"
		, "updId"
		, "updDt"
})
@Schema(name = "AccountDto", description = "계정 DTO")
@ToString(exclude = {
		"accountId"
})
public class AccountDto implements Serializable {

	private static final long serialVersionUID = -6605465516954409942L;

	@Schema(name = "accountId", description = "계정아이디")
	private long accountId;

	// 계정아이디
	@Schema(name = "username", description = "계정아이디")
	private String username;

	// 권한아이디
	@Schema(name = "authority", description = "권한아이디")
	private AuthorityDto authority;

	// 로그인이력
	@Schema(name = "loginHistoryList", description = "로그인이력")
	private List<LoginHistoryDto> loginHistoryList = new ArrayList<>();

	// 비밀번호
	@Schema(name = "password", description = "비밀번호")
	private String password;

	// 이름
	@Schema(name = "accountName", description = "이름")
	private String accountName;

	// 소속회사명
	@Schema(name = "companyName", description = "소속회사명")
	private String companyName;

	// 연락처
	@Schema(name = "phoneNumber", description = "연락처")
	private String phoneNumber;

	// 이메일
	@Schema(name = "email", description = "이메일")
	private String email;

	// 등록자아이디
	@Schema(name = "regId", description = "등록자아이디")
	private String regId;

	// 등록일시
	@Schema(name = "regDt", description = "등록일시")
	private LocalDateTime regDt;

	// 계정상태
	@Schema(name = "status", description = "계정상태")
	private String status;

	// 수정자아이디
	@Schema(name = "updId", description = "수정자아이디")
	private String updId;

	// 수정자아이디
	@Schema(name = "updDt", description = "수정자아이디")
	private LocalDateTime updDt;

	// 비밀번호 변경일시
	@Schema(name = "passwordUpdDt", description = "비밀번호 변경일시")
	private LocalDateTime passwordUpdDt;

	public void convertXss() {
		if(username != null) {
			username = TextUtils.convertXss(username);
		}
		if(authority != null) {
			authority.convertXss();
		}
		if(accountName != null) {
			accountName = TextUtils.convertXss(accountName);
		}
		if(phoneNumber != null) {
			phoneNumber = TextUtils.convertXss(phoneNumber);
		}
		if(email != null) {
			email = TextUtils.convertXss(email);
		}
	}
}
