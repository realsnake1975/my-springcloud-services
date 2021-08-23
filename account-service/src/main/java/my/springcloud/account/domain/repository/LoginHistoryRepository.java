package my.springcloud.account.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import my.springcloud.account.domain.entity.LoginHistory;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

	Optional<LoginHistory> findTop1ByAccountIdOrderByHistorySeqDesc(Long accountId);

	Optional<LoginHistory> findTop1ByAuthTokenOrderByHistorySeqDesc(String authToken);

	List<LoginHistory> findByAccountIdAndOtpNotNull(Long accountId);

	List<LoginHistory> findByLoginReqDtBefore(LocalDateTime beforeDt);

	List<LoginHistory> findByLoginReqDtBeforeAndOtpNotNullAndLoginDtNull(LocalDateTime beforeDt);

	//    @Modifying
	//    @Query(value = "UPDATE TB_LOGIN_HISTORY SET LOGIN_DT = NOW() WHERE AUTH_TOKEN = :authToken AND OTP = :otp", nativeQuery = true)
	//    void updateLoginDt(@Param("authToken") String authToken, @Param("otp") String otp);

}
