package my.springcloud.account.schedule;

import my.springcloud.account.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LockedAccountUnlockingTask {

    private final AuthService service;

    @Scheduled(cron = "1 * * * * *") // 매분 1초마다 실행
    public void runUnlockAccount() {
        this.service.unlockAccount();
    }

}
