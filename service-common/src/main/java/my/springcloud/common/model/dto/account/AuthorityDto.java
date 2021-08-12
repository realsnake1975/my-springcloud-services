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
		"authorityId"
		, "regId"
		, "regDt"
		, "updId"
		, "updDt"
})
@Schema(name = "AuthorityDto", description = "권한 DTO")
@ToString(exclude = {
		"authorityId"
		, "menuAuthorityList"
		, "regId"
		, "regDt"
		, "updId"
		, "updDt"
})
public class AuthorityDto implements Serializable {

	private static final long serialVersionUID = -6605465516954409942L;

	@Schema(name = "authorityId", description = "계정아이디")
	private long authorityId;

	// 메뉴권한
	@Schema(name = "menuAuthorityList", description = "메뉴권한")
	private List<MenuAuthorityDto> menuAuthorityList = new ArrayList<>();

	// 권한명
	@Schema(name = "authorityName", description = "권한명")
	private String authorityName;

	// 설명
	@Schema(name = "description", description = "설명")
	private String description;

	// 사용여부
	@Schema(name = "useYn", description = "사용여부")
	private Boolean useYn;

	// 등록자아이디
	@Schema(name = "regId", description = "등록자아이디")
	private String regId;

	// 등록일시
	@Schema(name = "LocalDateTime", description = "등록일시")
	private LocalDateTime regDt;

	// 수정자아이디
	@Schema(name = "updId", description = "수정자아이디")
	private String updId;

	// 수정일시
	@Schema(name = "updDt", description = "수정일시")
	private LocalDateTime updDt;

	public void convertXss() {
		if(authorityName != null) {
			authorityName = TextUtils.convertXss(authorityName);
		}
		if(description != null) {
			description = TextUtils.convertXss(description);
		}
	}
}
