package my.springcloud.common.model.account;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import my.springcloud.common.constants.AccountStatusType;

@Getter
@Setter
@JsonIgnoreProperties(value = {
	"accountId",
	"loginHistories",
	"regId",
	"regDt",
	"modId",
	"modDt",
	"status"
})
@Schema(name = "AccountCreate", description = "계정 등록 DTO")
public class AccountCreate extends AccountDetail {

	private static final long serialVersionUID = -5154790211167492937L;

	public AccountCreate() {
		LocalDateTime now = LocalDateTime.now();
		super.setRegDt(now);
		super.setUpdDt(now);
		super.setPasswordUpdDt(now);
		super.setStatus(AccountStatusType.APPROVAL.code());
	}

}
