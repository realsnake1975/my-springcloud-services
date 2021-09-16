package my.springcloud.account.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;
import my.springcloud.common.domain.AttachFile;

@Getter
@Setter
@Entity
@Table(name = "TB_ACCOUNT_ATTACH_FILE")
// @AttributeOverrides({
// 	@AttributeOverride(name="updatedName", column = @Column(name="changed_name")),
// 	@AttributeOverride(name="path", column = @Column(name="url"))
// })
public class AccountAttachFile extends AttachFile {

	private static final long serialVersionUID = -5801069088681512067L;

	@Id
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@GeneratedValue(generator = "uuid2")
	@Column(name = "attach_file_id")
	private String attachFileId;

	// 단방향 @OneToMany 관계에서 nullable = true일 때, TB_ACCOUNT_ATTACH_FILE 테이블 insert 후 account_id를 update 한다.
	@Column(name = "account_id")
	private Long accountId;

}
