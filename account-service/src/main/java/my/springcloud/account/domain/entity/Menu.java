package my.springcloud.account.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@BatchSize(size = 100)
@Entity
@Table(name = "TB_MENU")
public class Menu implements Serializable {

	private static final long serialVersionUID = 2389323026496370460L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_id")
	private long menuId;

	// 부모 메뉴
	@Column(name = "parent_menu_id")
	private Long parentMenuId;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "parent_menu_id")
	@OrderBy("sort_order asc")
	@BatchSize(size = 100)
	private List<Menu> menus = new ArrayList<>();

	// Depth
	@Column(name = "depth", nullable = false)
	private Integer depth;

	// 메뉴명
	@Column(name = "menu_name", nullable = false, length = 100)
	private String menuName;

	// 메뉴URL
	@Column(name = "menu_url", length = 100)
	private String menuUrl;

	// 정렬순서
	@Column(name = "sort_order", nullable = false)
	private Integer sortOrder;

}
