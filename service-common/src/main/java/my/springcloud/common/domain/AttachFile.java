package my.springcloud.common.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * 첨부파일
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AttachFile implements Serializable {

	private static final long serialVersionUID = -4525804881723586602L;

	@Column(name = "org_name", nullable = false, length = 300)
	private String orgName;

	@Column(name = "updated_name", length = 300)
	private String updatedName;

	@Column(name = "mime", nullable = false, length = 100)
	private String mime;

	@Column(name = "size", nullable = false)
	private long size;

	@Column(name = "path", length = 300)
	private String path;

}
