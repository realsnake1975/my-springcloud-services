package my.springcloud.config.jackson.deserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Optional;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

	private static final DateTimeFormatter DTF = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd")
		.toFormatter();

	@Override
	public LocalDate deserialize(JsonParser p, DeserializationContext dc) throws IOException {
		return this.convert(p.getText());
	}

	private LocalDate convert(@Nullable String text) {
		return Optional.ofNullable(text).map(s -> !s.isEmpty() ? LocalDate.parse(s, DTF) : null).orElse(null);
	}

}
