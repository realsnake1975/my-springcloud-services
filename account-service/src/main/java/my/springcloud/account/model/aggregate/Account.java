package my.springcloud.account.model.aggregate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_ACCOUNT")
@Getter
@Setter
public class Account extends AbstractAggregateRoot<Account> implements Serializable {

    private static final long serialVersionUID = 2389323026496370460L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private long accountId;

    // 계정아이디
    @Column(name = "username", nullable = false, length = 20)
    private String username;

    // 권한아이디
    @ManyToOne
    @JoinColumn(name = "authority_id")
    private Authority authority;

    // 비밀번호
    @Column(name = "password", nullable = false, length = 300)
    private String password;

    // 이름
    @Column(name = "account_name", nullable = false, length = 100)
    private String accountName;

    // 소속회사명
    @Column(name = "company_name", length = 100)
    private String companyName;

    // 연락처
    @Column(name = "phone_number", length = 300)
    private String phoneNumber;

    // 이메일
    @Column(name = "email", nullable = false, length = 300)
    private String email;

    // 등록자아이디
    @Column(name = "reg_id", nullable = false, length = 20)
    private String regId;

    // 계정상태
    @Column(name = "status", columnDefinition = "VARCHAR(20) DEFAULT 'approval'", nullable = false, length = 20)
    private String status;

    // 등록일시
    @Column(name = "reg_dt", nullable = false)
    private LocalDateTime regDt;

    // 수정자아이디
    @Column(name = "upd_id", length = 20)
    private String updId;

    // 수정일시
    @Column(name = "upd_dt")
    private LocalDateTime updDt;

    // 비밀번호 변경일시
    @Column(name = "password_upd_dt")
    private LocalDateTime passwordUpdDt;

    // 계정 잠금일시
    @Column(name = "account_locked_dt")
    private LocalDateTime accountLockedDt;

}
