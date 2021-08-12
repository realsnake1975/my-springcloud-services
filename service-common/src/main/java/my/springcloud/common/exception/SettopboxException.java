package my.springcloud.common.exception;

import my.springcloud.common.constants.ResponseCodeType;
import lombok.Getter;

public class SettopboxException extends RuntimeException {

    @Getter
    private final ResponseCodeType responseCodeType;

    public SettopboxException(ResponseCodeType responseCodeType) {
        super(responseCodeType.desc());
        this.responseCodeType = responseCodeType;
    }

    public SettopboxException(ResponseCodeType responseCodeType, Throwable e) {
        super(e);
        this.responseCodeType = responseCodeType;
    }

}
