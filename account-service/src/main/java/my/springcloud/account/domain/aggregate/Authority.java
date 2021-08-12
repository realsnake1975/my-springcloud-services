package my.springcloud.account.domain.aggregate;

import my.springcloud.account.domain.entity.MenuAuthority;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@BatchSize(size = 20)
@Entity
@Table(name = "TB_AUTHORITY")
public class Authority extends AbstractAggregateRoot<Authority> implements Serializable {

    private static final long serialVersionUID = 2389323026496370460L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private long authorityId;

    // 메뉴권한
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "authority_id")
    @BatchSize(size = 100)
    private List<MenuAuthority> menuAuthorityList = new ArrayList<>();

    // 권한명
    @Column(name = "authority_name", nullable = false, length = 100)
    private String authorityName;

    // 설명
    @Column(name = "description", nullable = false, length = 300)
    private String description;

    // 사용여부
    @Column(name = "use_yn", nullable = false)
    private Boolean useYn;

    // 등록자아이디
    @Column(name = "reg_id", nullable = false, length = 20)
    private String regId;

    // 등록일시
    @Column(name = "reg_dt", nullable = false)
    private LocalDateTime regDt;

    // 수정자아이디
    @Column(name = "upd_id", length = 20)
    private String updId;

    // 수정일시
    @Column(name = "upd_dt")
    private LocalDateTime updDt;

}
