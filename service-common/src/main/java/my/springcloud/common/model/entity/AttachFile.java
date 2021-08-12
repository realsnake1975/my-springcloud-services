package my.springcloud.common.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 첨부파일
 */
@MappedSuperclass
@Getter
@Setter
public class AttachFile implements Serializable {

    private static final long serialVersionUID = -4525804881723586602L;

    @Column(name = "org_name", nullable = false, length = 300)
    private String orgName;

    @Column(name = "mime", nullable = false, length = 100)
    private String mime;

    @Column(name = "size", nullable = false)
    private long size;

}
