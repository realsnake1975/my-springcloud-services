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
public class AttachFile implements Serializable {

	private static final long serialVersionUID = -4525804881723586602L;

	@Column(name = "org_name", nullable = false, length = 300)
	private String orgName;

	@Column(name = "mime", nullable = false, length = 100)
	private String mime;

	@Column(name = "size", nullable = false)
	private long size;

}
