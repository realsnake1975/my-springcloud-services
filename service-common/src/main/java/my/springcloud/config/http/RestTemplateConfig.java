package my.springcloud.config.http;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import my.springcloud.config.http.proxy.ProxyProperties;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

	private final ProxyProperties proxyProperties;
	@Value("${http.connection-timeout:5000}")
	private int connectionTimeout;
	@Value("${http.read-timeout:5000}")
	private int readTimeout;

	@Profile("dev | cbt | prod")
	@Bean
	public RestTemplate restTemplate() {
		//        ProxyInfo proxyInfo = new ProxyInfo(proxyProperties.getHost(), proxyProperties.getPort());
		//
		//        HttpComponentsClientHttpRequestFactory factory =
		//                new HttpComponentsClientHttpRequestFactory(
		//                        HttpClientBuilder.create()
		//                                .setRoutePlanner(new SystemDefaultRoutePlanner(new Selector(Arrays.stream(new ProxyInfo[] { proxyInfo }).collect(Collectors.toList()), this.proxyProperties.getTarget())))
		//                                .build()
		//                );

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
			HttpClientBuilder.create().build());
		factory.setConnectionRequestTimeout(this.connectionTimeout);  // 연결 요청 지연이 5초를 초과하면 Exception
		factory.setConnectTimeout(this.connectionTimeout); // 서버에 연결 지연이 5초를 초과하면 Exception
		factory.setReadTimeout(this.readTimeout); // 데이터 수신 지연이 5초를 초과하면 Exception
		return new RestTemplate(factory);
	}

	@Profile("local")
	@Bean
	public RestTemplate restTemplateLocal() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
			HttpClientBuilder.create().build());
		factory.setConnectionRequestTimeout(this.connectionTimeout); // 연결 요청 지연이 5초를 초과하면 Exception
		factory.setConnectTimeout(this.connectionTimeout); // 서버에 연결 지연이 5초를 초과하면 Exception
		factory.setReadTimeout(this.readTimeout); // 데이터 수신 지연이 5초를 초과하면 Exception
		return new RestTemplate(factory);
	}

}
