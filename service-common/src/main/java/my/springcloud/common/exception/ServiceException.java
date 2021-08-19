package my.springcloud.common.exception;

import lombok.Getter;
import my.springcloud.common.constants.ResponseCodeType;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -8765672124498590564L;

	@Getter
	private final ResponseCodeType responseCodeType;

	public ServiceException(ResponseCodeType responseCodeType, Throwable e) {
		super(e);
		this.responseCodeType = responseCodeType;
	}

	public ServiceException(ResponseCodeType responseCodeType) {
		this.responseCodeType = responseCodeType;
	}

}
