package my.springcloud.account.config;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import my.springcloud.config.servlet.ResponseSuccessAdvice;

@RestControllerAdvice(
	basePackages = {
		"my.springcloud.account.controller",
	}
)
public class SuccessBodyAdvice extends ResponseSuccessAdvice {

}
