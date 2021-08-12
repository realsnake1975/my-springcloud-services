package my.springcloud.common.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class CustomEvent<T> {

	private final String id;
	private final LocalDateTime publishedDt;
	@Setter
	private String type;
	private final T event;

	public CustomEvent(T event) {
		this.id = UUID.randomUUID().toString();
		this.publishedDt = LocalDateTime.now();
		this.event = event;
	}

	public CustomEvent(String type, T event) {
		this.id = UUID.randomUUID().toString();
		this.publishedDt = LocalDateTime.now();
		this.type = type;
		this.event = event;
	}

}
