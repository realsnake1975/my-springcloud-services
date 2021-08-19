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
	"authorityId"
	, "regId"
	, "regDt"
	, "updId"
	, "updDt"
})
@ToString(exclude = {
	"authorityId"
	, "menuAuthorityList"
	, "regId"
	, "regDt"
	, "updId"
	, "updDt"
})
@Schema(name = "AuthorityDto", description = "권한 DTO")
public class AuthorityDetail implements Serializable {

	private static final long serialVersionUID = -6605465516954409942L;

	@Schema(name = "authorityId", description = "계정아이디")
	private long authorityId;

	@Schema(name = "menuAuthorityList", description = "메뉴권한")
	private List<MenuAuthorityDetail> menuAuthorityList = new ArrayList<>();

	@Schema(name = "authorityName", description = "권한명")
	private String authorityName;

	@Schema(name = "description", description = "설명")
	private String description;

	@Schema(name = "useYn", description = "사용여부")
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
		if (authorityName != null) {
			authorityName = TextUtils.convertXss(authorityName);
		}
		if (description != null) {
			description = TextUtils.convertXss(description);
		}
	}

}
