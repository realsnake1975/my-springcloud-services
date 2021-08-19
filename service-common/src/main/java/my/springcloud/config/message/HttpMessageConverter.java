package my.springcloud.config.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class HttpMessageConverter {

	private final ObjectMapper objectMapper;

	@Autowired
	public HttpMessageConverter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Primary
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		return new MappingJackson2HttpMessageConverter(objectMapper);
	}

}
