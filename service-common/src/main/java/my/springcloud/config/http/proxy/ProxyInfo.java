package my.springcloud.config.http.proxy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProxyInfo {

	private final String proxyHost;
	private final Integer proxyPort;

}
