package my.springcloud.account.exception;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.config.exception.CommonErrorController;

/**
 * ApiExceptionHandler 등에서 처리하지 않는 엑셉션에 대해 처리
 */
@Hidden
@Slf4j
@RestController
public class CustomErrorController extends CommonErrorController {

	public CustomErrorController(ObjectMapper objectMapper) {
		super(objectMapper);
	}

}
