package my.springcloud.account.config;

import my.springcloud.config.servlet.ResponseSuccessAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(
        basePackages = {
				"my.springcloud.account.controller",
        }
)
public class SuccessBodyAdvice extends ResponseSuccessAdvice {

}
