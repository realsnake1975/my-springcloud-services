package my.springcloud.common.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomEventListener {

	@TransactionalEventListener
	public void onChanged(CustomEvent<?> event) {
		log.debug("> onChanged.");
	}

}
