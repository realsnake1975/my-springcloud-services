package my.springcloud.common.model.account;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import my.springcloud.common.utils.TextUtils;

@Getter
@Setter
@EqualsAndHashCode(exclude = {
	"accountId",
	"username",
	"loginHistories",
	"accountName",
	"companyName",
	"status",
	"regId",
	"regDt",
	"updId",
	"updDt",
	"attachFiles"
})
@ToString(exclude = {
	"accountId",
	"loginHistories",
	"attachFiles"
})
@JsonIgnoreProperties(value = {
	"loginHistories"
})
@Schema(name = "AccountDetail", description = "계정 상세 DTO")
public class AccountDetail implements Serializable {

	private static final long serialVersionUID = -6605465516954409942L;

	@Schema(name = "accountId", description = "계정 일련번호")
	private long accountId;

	@Schema(name = "username", description = "계정 아이디", required = true, minLength = 4, maxLength = 15)
	private String username;

	@Schema(name = "authority", description = "권한")
	private AuthorityDetail authority;

	@Schema(name = "loginHistories", description = "로그인 이력")
	private List<LoginHistoryDetail> loginHistories = new ArrayList<>();

	@Schema(name = "password", description = "비밀번호", required = true, minLength = 8, maxLength = 20, format = "password")
	private String password;

	@Schema(name = "accountName", description = "이름", required = true)
	private String accountName;

	@Schema(name = "companyName", description = "소속회사명", required = true)
	private String companyName;

	@Schema(name = "phoneNumber", description = "연락처", required = true)
	private String phoneNumber;

	@Schema(name = "email", description = "이메일", required = true, format = "email")
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

	@Schema(name = "attachFiles", description = "첨부파일 목록")
	private List<AccountAttachFileDetail> attachFiles = new ArrayList<>();

	private void convertXss() {
		if (Objects.nonNull(username)) {
			username = TextUtils.convertXss(username);
		}
		if (Objects.nonNull(authority)) {
			authority.convertXss();
		}
		if (Objects.nonNull(accountName)) {
			accountName = TextUtils.convertXss(accountName);
		}
		if (Objects.nonNull(phoneNumber)) {
			phoneNumber = TextUtils.convertXss(phoneNumber);
		}
		if (Objects.nonNull(email)) {
			email = TextUtils.convertXss(email);
		}
	}

	public AccountDetail convertXssAndPasswordEmpty() {
		this.convertXss();
		this.password = "";
		return this;
	}

}
