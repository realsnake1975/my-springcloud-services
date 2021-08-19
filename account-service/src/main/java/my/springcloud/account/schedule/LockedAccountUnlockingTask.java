package my.springcloud.account.schedule;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.account.service.AuthService;

@Slf4j
@RequiredArgsConstructor
@Service
public class LockedAccountUnlockingTask {

	private final AuthService service;

	// @Scheduled(cron = "1 * * * * *") // 매분 1초마다 실행
	public void runUnlockAccount() {
		this.service.unlockAccount();
	}

}
