package my.springcloud.account.schedule;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.service.LoginHistoryService;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginHistoryDeletingTask {

	private final LoginHistoryService service;

	// @Scheduled(cron = "5 * * * * *") // 매분 5초마다 실행
	public void runDeleteLoginHistory() {
		this.service.deleteLoginHistory();
	}

}
