package my.springcloud.account.schedule;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.service.LoginHistoryService;

@Slf4j
@RequiredArgsConstructor
@Component
public class OtpDestroyingTask {

	private final LoginHistoryService service;

	// @Scheduled(cron = "1 * * * * *") // 매분 1초마다 실행
	public void runDestroyOtp() {
		this.service.destroyOtp();
	}

}
