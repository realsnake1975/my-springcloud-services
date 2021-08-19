package my.springcloud.common.event;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CustomEvent<T> {

	private final String id;
	private final LocalDateTime publishedDt;
	private final T event;
	@Setter
	private String type;

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
