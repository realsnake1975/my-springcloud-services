package my.springcloud.config.jackson;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import my.springcloud.config.jackson.module.CustomModule;
import my.springcloud.config.jackson.module.LocalDateTimeModule;

@Configuration
public class JacksonMapperConfig {

	@Primary
	@Bean(name = "jacksonObjectMapper")
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
			.json()
			.featuresToEnable(SerializationFeature.INDENT_OUTPUT)
			.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.modules(
				new ParameterNamesModule(),
				new Jdk8Module(),
				new JavaTimeModule(),
				new LocalDateTimeModule(),
				new CustomModule(),
				new AfterburnerModule()
			)
			.indentOutput(true)
			.failOnEmptyBeans(false)
			.failOnUnknownProperties(false)
			.build();

		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		return objectMapper;
	}

}
