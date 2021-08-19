package my.springcloud.config.http.proxy;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "proxy")
public class ProxyProperties {

	private String host;
	private Integer port;
	private List<String> target;

}
