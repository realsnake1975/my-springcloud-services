package my.springcloud.config.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter DTF1 = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss").toFormatter();
    private static final DateTimeFormatter DTF2 = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").toFormatter();

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext dc) throws IOException {
        return this.convert(p.getText());
    }

    private LocalDateTime convert(@Nullable String text) {
		if (text == null || text.isEmpty()) {
			return null;
		}

        try {
			if (text.contains("T")) {
				return LocalDateTime.parse(text, DTF1);
            }
            else {
				return LocalDateTime.parse(text, DTF2);
            }
        }
        catch (Exception e) {
            return null;
        }
    }

}
