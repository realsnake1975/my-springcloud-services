package my.springcloud;

import java.text.NumberFormat;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));

		Runtime runtime = Runtime.getRuntime();

		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		// long freeMemory = runtime.freeMemory();
		long mb = 1048576; // 1024 * 1024
		String mega = " MB";

		String mm = NumberFormat.getInstance().format(maxMemory / mb) + mega;
		String am = NumberFormat.getInstance().format(allocatedMemory / mb) + mega;

		log.debug("> available processor count: {}, memeory - max: {}, allocated: {}", runtime.availableProcessors(),
			mm, am);
	}

}
