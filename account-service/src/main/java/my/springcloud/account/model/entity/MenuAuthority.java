package my.springcloud.account.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "TB_MENU_AUTHORITY")
@Getter
@Setter
@Entity
@BatchSize(size = 10)
public class MenuAuthority implements Serializable {

	private static final long serialVersionUID = 2389323026496370460L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_authority_id")
	private long menuAuthorityId;

	@ManyToOne
	@JoinColumn(name = "menu_id")
	private Menu menu;

	// 권한아이디
	@Column(name = "authority_id", nullable = false)
	private Long authorityId;

	// 열람권한여부
	@Column(name = "read_yn", nullable = false)
	private boolean readYn;

	// 제어권한여부
	@Column(name = "control_yn", nullable = false)
	private boolean controlYn;
}
