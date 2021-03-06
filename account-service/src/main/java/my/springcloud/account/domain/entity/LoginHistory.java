package my.springcloud.account.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TB_LOGIN_HISTORY")
public class LoginHistory implements Serializable {

	private static final long serialVersionUID = 2389323026496370460L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "history_seq")
	private Long historySeq;

	// 계정아이디
	@Column(name = "account_id", nullable = false)
	private long accountId;

	// 로그인요청일시
	@Column(name = "login_req_dt", nullable = false)
	private LocalDateTime loginReqDt;

	// 로그인실패횟수
	@Column(name = "login_fail_cnt", columnDefinition = "int default 0")
	private Integer loginFailCnt;

	// 인증토큰
	@Column(name = "auth_token")
	private String authToken;

	// OTP
	@Column(name = "otp")
	private String otp;

	// OTP인증시도횟수
	@Column(name = "otp_auth_try_cnt", columnDefinition = "int default 0")
	private Integer otpAuthTryCnt;

	// OTP인증실패횟수
	@Column(name = "otp_auth_fail_cnt", columnDefinition = "int default 0")
	private Integer otpAuthFailCnt;

	// 로그인일시
	@Column(name = "login_dt")
	private LocalDateTime loginDt;

}
