package my.springcloud.account.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.exception.ServiceException;
import my.springcloud.common.wrapper.CommonModel;
import my.springcloud.config.exception.ApiExceptionHandler;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ServiceExceptionHandler extends ApiExceptionHandler {

	@SuppressWarnings("rawtypes")
	@ExceptionHandler({ServiceException.class})
	public ResponseEntity handleServiceException(HttpServletRequest req, ServiceException e) {
		log.error(req.getRequestURL() + " ServiceException", e);

		CommonModel scm = new CommonModel(e.getResponseCodeType());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scm);
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler({BadCredentialsException.class})
	public ResponseEntity handleBadCredentialsException(HttpServletRequest req, BadCredentialsException e) {
		log.error(req.getRequestURL() + " BadCredentialsException: {}", e.getMessage());

		CommonModel cm = new CommonModel(ResponseCodeType.SERVER_ERROR_41001001);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(cm);
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler({AdminAuthException.class})
	public ResponseEntity handleAdminAuthException(HttpServletRequest req, AdminAuthException e) {
		log.error(req.getRequestURL() + " AdminAuthException: {}", e.getMessage());

		CommonModel cm = new CommonModel(e.getResponseCodeType());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(cm);
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler({CannotCreateTransactionException.class})
	public ResponseEntity handleCannotCreateTransactionException(HttpServletRequest req,
		CannotCreateTransactionException e) {
		log.error(req.getRequestURL() + " CannotCreateTransactionException", e);

		CommonModel scm = new CommonModel(ResponseCodeType.SERVER_ERROR_43001001);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scm);
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler({MissingRequestHeaderException.class})
	public ResponseEntity handleMissingRequestHeaderException(HttpServletRequest req, MissingRequestHeaderException e) {
		log.error(req.getRequestURL() + " MissingRequestHeaderException", e);

		CommonModel scm = new CommonModel(ResponseCodeType.SERVER_ERROR_41001001);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(scm);
	}

	@Override
	@SuppressWarnings("rawtypes")
	@ExceptionHandler({HttpMessageNotReadableException.class})
	public ResponseEntity invalidFormatException(HttpServletRequest req, HttpMessageNotReadableException e) {
		log.error(req.getRequestURL() + " HttpMessageNotReadableException: {}", e.getMessage());

		CommonModel scm = new CommonModel(ResponseCodeType.SERVER_ERROR_42001009);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(scm);
	}

}
