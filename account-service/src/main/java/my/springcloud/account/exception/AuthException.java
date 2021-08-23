package my.springcloud.account.exception;

import org.springframework.security.core.AuthenticationException;

import lombok.Getter;
import my.springcloud.common.constants.ResponseCodeType;

public class AuthException extends AuthenticationException {

	@Getter
	private final ResponseCodeType responseCodeType;

	public AuthException(ResponseCodeType responseCodeType) {
		super(responseCodeType.desc());
		this.responseCodeType = responseCodeType;
	}

}
