package my.springcloud.config.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class LocalDateTimeToFormatSerializer extends JsonSerializer<LocalDateTime> {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeString(this.localDateTimeToString(value, DTF));
    }

    public String localDateTimeToString(@Nullable LocalDateTime localDateTime, @Nullable DateTimeFormatter formatter) {
        return Optional.ofNullable(localDateTime).map(m -> m.format(Optional.ofNullable(formatter).orElse(DTF))).orElse("");
    }

}
