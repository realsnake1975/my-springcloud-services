package my.springcloud.common.model.account;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import my.springcloud.common.utils.TextUtils;

@Getter
@Setter
@EqualsAndHashCode(exclude = {
	"authorityId",
	"regId",
	"regDt",
	"updId",
	"updDt"
})
@ToString(exclude = {
	"authorityId",
	"menuAuthorities",
	"regId",
	"regDt",
	"updId",
	"updDt"
})
@Schema(name = "AuthorityDetail", description = "권한 상세 DTO")
public class AuthorityDetail implements Serializable {

	private static final long serialVersionUID = -6605465516954409942L;

	@Schema(name = "authorityId", description = "권한 일련번호")
	private long authorityId;

	@Schema(name = "menuAuthorities", description = "메뉴권한")
	private List<MenuAuthorityDetail> menuAuthorities = new ArrayList<>();

	@Schema(name = "authorityName", description = "권한명", required = true)
	private String authorityName;

	@Schema(name = "description", description = "설명")
	private String description;

	@Schema(name = "useYn", description = "사용여부", required = true)
	private Boolean useYn;

	@Schema(name = "regId", description = "등록자아이디")
	private String regId;

	@Schema(name = "LocalDateTime", description = "등록일시")
	private LocalDateTime regDt;

	@Schema(name = "updId", description = "수정자아이디")
	private String updId;

	@Schema(name = "updDt", description = "수정일시")
	private LocalDateTime updDt;

	public void convertXss() {
		if (Objects.nonNull(authorityName)) {
			authorityName = TextUtils.convertXss(authorityName);
		}
		if (Objects.nonNull(description)) {
			description = TextUtils.convertXss(description);
		}
	}

}
