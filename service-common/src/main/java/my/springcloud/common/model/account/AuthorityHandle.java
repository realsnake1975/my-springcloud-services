package my.springcloud.common.model.account;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(value = {
	"authorityId"
	, "accountList"
	, "menuAuthorityList"
	, "regId"
	, "regDt"
	, "modId"
	, "modDt"
})
@Schema(name = "AuthorityHandleDto", description = "권한 DTO 등록, 수정")
public class AuthorityHandle extends AuthorityDetail {

	private static final long serialVersionUID = -5154790211167492937L;
	private List<Boolean> readYn;
	private List<Boolean> controlYn;

	public AuthorityHandle() {
		LocalDateTime now = LocalDateTime.now();
		setRegDt(now);
		setUpdDt(now);
	}

}
