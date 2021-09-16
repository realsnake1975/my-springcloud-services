package my.springcloud.common.model.account;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import my.springcloud.common.constants.AccountStatusType;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = {
	"accountId",
	"authority",
	"loginHistories",
	"status",
	"regId",
	"regDt",
	"updId",
	"updDt",
	"passwordUpdDt",
	"accountLockedDt",
	"attachFiles"
})
@Schema(name = "AccountCreate", description = "계정 등록 DTO")
public class AccountCreate extends AccountDetail {

	private static final long serialVersionUID = -5154790211167492937L;

	@Schema(name = "authorityId", description = "권한 일련번호", required = true, type = "integer", format = "int64")
	private long authorityId;

	public AccountCreate() {
		LocalDateTime now = LocalDateTime.now();
		super.setRegDt(now);
		super.setStatus(AccountStatusType.APPROVAL.code());
	}

}
