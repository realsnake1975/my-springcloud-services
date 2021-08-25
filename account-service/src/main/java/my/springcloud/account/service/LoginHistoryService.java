package my.springcloud.account.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.domain.entity.LoginHistory;
import my.springcloud.account.domain.repository.LoginHistoryRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class LoginHistoryService {

	private final LoginHistoryRepository repository;

	public void deleteLoginHistory() {
		LocalDateTime datetimeBefore = LocalDateTime.now().minusDays(90L);
		log.debug("> 로그인 히스토리 삭제, datetimeBefore: {}", datetimeBefore);

		List<LoginHistory> deletingLoginHistories = this.repository.findByLoginReqDtBefore(datetimeBefore);

		deletingLoginHistories.forEach(h -> log.debug("> 삭제할 로그인 히스토리: {}, {}", h.getHistorySeq(), h.getLoginReqDt()));

		this.repository.deleteAll(deletingLoginHistories);
	}

	public void destroyOtp() {
		LocalDateTime datetimeBefore = LocalDateTime.now().minusMinutes(5L);
		log.debug("> 인증되지 않은 OTP 파기, datetimeBefore: {}", datetimeBefore);

		List<LoginHistory> loginHistories = this.repository.findByLoginReqDtBeforeAndOtpNotNullAndLoginDtNull(datetimeBefore);
		loginHistories.forEach(h -> h.setOtp(null));
	}

}
