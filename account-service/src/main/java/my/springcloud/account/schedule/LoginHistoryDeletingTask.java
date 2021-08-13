package my.springcloud.account.schedule;

import my.springcloud.account.service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
