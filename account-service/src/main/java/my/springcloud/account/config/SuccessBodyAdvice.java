package my.springcloud.account.config;

import my.springcloud.config.servlet.ResponseSuccessAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(
        basePackages = {
				"com.lguplus.homeshoppingmoa.operation.controller",
        }
)
public class SuccessBodyAdvice extends ResponseSuccessAdvice {

}
