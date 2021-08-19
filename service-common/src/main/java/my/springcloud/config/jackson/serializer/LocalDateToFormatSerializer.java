package my.springcloud.config.jackson.serializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LocalDateToFormatSerializer extends JsonSerializer<LocalDate> {

	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeString(this.localDateToString(value, DTF));
	}

	public String localDateToString(@Nullable LocalDate localDate, @Nullable DateTimeFormatter formatter) {
		return Optional.ofNullable(localDate).map(m -> m.format(Optional.ofNullable(formatter).orElse(DTF))).orElse("");
	}

}
