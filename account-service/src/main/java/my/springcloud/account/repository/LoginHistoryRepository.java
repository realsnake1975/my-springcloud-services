package my.springcloud.account.repository;

import my.springcloud.account.model.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    Optional<LoginHistory> findTop1ByAccountIdOrderByHistorySeqDesc(Long accountId);

    Optional<LoginHistory> findTop1ByAuthTokenOrderByHistorySeqDesc(String authToken);

    List<LoginHistory> findByAccountId(Long accountId);

    List<LoginHistory> findByAccountIdAndOtpNotNull(Long accountId);

    List<LoginHistory> findByLoginReqDtBefore(LocalDateTime beforeDt);

    List<LoginHistory> findByLoginReqDtBeforeAndOtpNotNullAndLoginDtNull(LocalDateTime beforeDt);

//    @Modifying
//    @Query(value = "UPDATE TB_LOGIN_HISTORY SET LOGIN_DT = NOW() WHERE AUTH_TOKEN = :authToken AND OTP = :otp", nativeQuery = true)
//    void updateLoginDt(@Param("authToken") String authToken, @Param("otp") String otp);

}
