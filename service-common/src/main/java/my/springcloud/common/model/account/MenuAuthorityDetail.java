package my.springcloud.common.model.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(exclude = {
		"menuAuthorityId"
})
@ToString(exclude = {
		"menuAuthorityId"
})
@Schema(name = "MenuAuthorityDto", description = "메뉴권한 DTO")
public class MenuAuthorityDetail implements Serializable {

	private static final long serialVersionUID = -6605465516954409942L;

	@Schema(name = "menuAuthorityId", description = "메뉴권한아이디")
	private long menuAuthorityId;

	@Schema(name = "authorityId", description = "권한아이디")
	private Long authorityId;

	@Schema(name = "menuId", description = "메뉴아이디")
	private Long menuId;

	@Schema(name = "readYn", description = "열람권한여부")
	private boolean readYn;

	@Schema(name = "controlYn", description = "제어권한여부")
	private boolean controlYn;

}
