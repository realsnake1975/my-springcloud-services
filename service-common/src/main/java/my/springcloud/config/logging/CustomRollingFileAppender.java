package my.springcloud.config.logging;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

import ch.qos.logback.core.rolling.RollingFileAppender;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.common.constants.CommonConstants;
import my.springcloud.common.constants.DtfType;

/**
 * https://ronanquillevere.github.io/2015/08/04/rolling-log-15-min.html#.YOag2egzbZS
 * @param <E>
 */
@Slf4j(topic = CommonConstants.LOGGER_NAME)
@Component
public class CustomRollingFileAppender<E> extends RollingFileAppender<E> {

	private static final int ROLL_OVER_MINUTES = 5;
	private final AtomicReference<LocalDateTime> startTime = new AtomicReference<>(LocalDateTime.now());

	@Override
	public void rollover() {
		LocalDateTime nowDatetime = LocalDateTime.parse(LocalDateTime.now().format(DtfType.yyyyMMddHHmm.dtf()),
			DtfType.yyyyMMddHHmm.dtf());
		LocalDateTime compDatetime = LocalDateTime.parse(startTime.get().format(DtfType.yyyyMMddHHmm.dtf()),
			DtfType.yyyyMMddHHmm.dtf());
		long diff = ChronoUnit.MINUTES.between(compDatetime, nowDatetime);

		if (diff >= ROLL_OVER_MINUTES) {
			super.rollover();
			startTime.set(nowDatetime);
		}
	}

	//    @Scheduled(fixedRate = 300000)
	//    public void runRollover() {
	//        log.info("");
	//    }

}
