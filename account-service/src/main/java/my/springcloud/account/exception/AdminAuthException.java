package my.springcloud.account.exception;

import my.springcloud.common.constants.ResponseCodeType;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

public class AdminAuthException extends AuthenticationException {

    @Getter
    private final ResponseCodeType responseCodeType;

    public AdminAuthException(ResponseCodeType responseCodeType) {
        super(responseCodeType.desc());
        this.responseCodeType = responseCodeType;
    }

}
