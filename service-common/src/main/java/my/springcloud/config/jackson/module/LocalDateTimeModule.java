package my.springcloud.config.jackson.module;

import com.fasterxml.jackson.databind.module.SimpleModule;
import my.springcloud.config.jackson.deserializer.LocalDateDeserializer;
import my.springcloud.config.jackson.deserializer.LocalDateTimeDeserializer;
import my.springcloud.config.jackson.serializer.LocalDateTimeToFormatSerializer;
import my.springcloud.config.jackson.serializer.LocalDateToFormatSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateTimeModule extends SimpleModule {

	private static final long serialVersionUID = 4594821473585999416L;

	public LocalDateTimeModule() {
        super();
		addSerializer(LocalDateTime.class, new LocalDateTimeToFormatSerializer());
		addSerializer(LocalDate.class, new LocalDateToFormatSerializer());
        addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
		addDeserializer(LocalDate.class, new LocalDateDeserializer());
    }

}
