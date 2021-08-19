package my.springcloud.account.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@BatchSize(size = 10)
@Entity
@Table(name = "TB_MENU_AUTHORITY")
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
