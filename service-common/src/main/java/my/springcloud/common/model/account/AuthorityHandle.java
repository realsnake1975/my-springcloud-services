package my.springcloud.common.model.account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = {
	"authorityId",
	"menuAuthorities",
	"regId",
	"regDt",
	"updId",
	"updDt"
})
@Schema(name = "AuthorityHandle", description = "권한 등록/수정 DTO")
public class AuthorityHandle extends AuthorityDetail {

	private static final long serialVersionUID = -5154790211167492937L;

	@ArraySchema(schema = @Schema(name = "readYn", description = "읽기여부", required = true, type = "boolean"))
	private List<Boolean> readYn = new ArrayList<>();

	@ArraySchema(schema = @Schema(name = "controlYn", description = "쓰기여부", required = true, type = "boolean"))
	private List<Boolean> controlYn = new ArrayList<>();

	public AuthorityHandle() {
		LocalDateTime now = LocalDateTime.now();
		super.setRegDt(now);
		super.setUpdDt(now);
	}

}
