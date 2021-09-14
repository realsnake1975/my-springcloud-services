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
	"username",
	"authority",
	"loginHistories",
	"status",
	"regId",
	"regDt",
	"updId",
	"updDt",
	"passwordUpdDt",
	"accountLockedDt"
})
@Schema(name = "AccountModify", description = "계정 수정 DTO")
public class AccountModify extends AccountDetail {

	private static final long serialVersionUID = -5154790211167492937L;

	@Schema(name = "authorityId", description = "권한 일련번호", required = true, type = "integer", format = "int64")
	private long authorityId;

	public AccountModify() {
		super.setUpdDt(LocalDateTime.now());
	}

}
