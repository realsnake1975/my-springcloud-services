package my.springcloud.common.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(value = {
	"accountId",
	"username",
	"loginHistories",
	"accountName",
	"companyName",
	"regId",
	"regDt",
	"modId",
	"modDt",
	"status"
})
@Schema(name = "AccountModify", description = "계정 수정 DTO")
public class AccountModify extends AccountDetail {

	private static final long serialVersionUID = -5154790211167492937L;

}
