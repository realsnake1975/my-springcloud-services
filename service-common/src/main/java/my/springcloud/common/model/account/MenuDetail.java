package my.springcloud.common.model.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = {
		"menuId"
})
@ToString(exclude = {
		"menuId"
})
@Schema(name = "MenuDto", description = "메뉴 DTO")
public class MenuDetail implements Serializable {

	private static final long serialVersionUID = -6605465516954409942L;

	@Schema(name = "menuId", description = "계정아이디")
	private long menuId;

	@Schema(name = "parentMenuId", description = "부모 메뉴")
	private Long parentMenuId;

	@Schema(name = "menus", description = "자식 메뉴")
	private List<MenuDetail> menus = new ArrayList<>();

	@Schema(name = "depth", description = "Depth")
	private Integer depth;

	@Schema(name = "menuName", description = "메뉴명")
	private String menuName;

	@Schema(name = "menuUrl", description = "메뉴URL")
	private String menuUrl;

	@Schema(name = "sortOrder", description = "정렬순서")
	private Integer sortOrder;

}
