package my.springcloud.config.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.config.jackson.JacksonMapperConfig;
import feign.FeignException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.springcloud.common.exception.ResourceNotFoundException;
import my.springcloud.common.wrapper.CommonModel;

import lombok.extern.slf4j.Slf4j;

/**
 * 공통 API 익셉션 핸들러
 */
@Slf4j
public class ApiExceptionHandler {

    private final ObjectMapper objectMapper = new JacksonMapperConfig().objectMapper();

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    @SuppressWarnings("rawtypes")
	@ExceptionHandler({Exception.class})
    public ResponseEntity exception(HttpServletRequest req, Exception e) {
        log.error(req.getRequestURL() + " Excetion", e);

        CommonModel cm = new CommonModel(HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cm);
    }

    @SuppressWarnings("rawtypes")
	@ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity noHandlerFoundException(HttpServletRequest req, NoHandlerFoundException e) {
        log.error(req.getRequestURL() + " NoHandlerFoundException: {}", e.getMessage());

        CommonModel cm = new CommonModel(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cm);
    }

    @SuppressWarnings("rawtypes")
	@ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity invalidFormatException(HttpServletRequest req, HttpMessageNotReadableException e) {
        log.error(req.getRequestURL() + " HttpMessageNotReadableException: {}", e.getMessage());

		CommonModel cm = new CommonModel(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cm);
    }

    @SuppressWarnings("rawtypes")
	@ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity validException(HttpServletRequest req, MethodArgumentNotValidException e) {
        log.error(req.getRequestURL() + " MethodArgumentNotValidException: {}", e.getMessage());

		CommonModel cm = new CommonModel(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cm);
    }

    @SuppressWarnings("rawtypes")
	@ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity resourceNotFoundException(HttpServletRequest req, ResourceNotFoundException e) {
        log.error(req.getRequestURL() + " ResourceNotFoundException: {}", e.getMessage());

		CommonModel cm = new CommonModel(HttpStatus.NOT_FOUND);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cm);
    }

    @SuppressWarnings("rawtypes")
	@ExceptionHandler({PropertyReferenceException.class})
    public ResponseEntity propertyReferenceException(HttpServletRequest req, PropertyReferenceException e) {
        log.error(req.getRequestURL() + " PropertyReferenceException: {}", e.getMessage());

		CommonModel cm = new CommonModel(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cm);
    }

    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@ExceptionHandler({BindException.class})
    public ResponseEntity validBindException(HttpServletRequest req, BindException e) {
        // validation이 잡히지 않아 별도로 실행
        log.error(req.getRequestURL() + " BindException: {}", e.getMessage());

		ArrayList<Map<String, Object>> messageList = new ArrayList<>();

        BindingResult errors = e.getBindingResult();
        if (errors.hasErrors()) {
            List<FieldError> error = errors.getFieldErrors();
            for (FieldError fieldError : error) {
				Map<String, Object> errorTemp = new HashMap();
                errorTemp.put("field", fieldError.getField());
                errorTemp.put("message", fieldError.getDefaultMessage());
                messageList.add(errorTemp);
            }
        }

        try {
			String jsonString = this.objectMapper.writeValueAsString(messageList);
			log.warn(jsonString);
        }
		catch (JsonProcessingException jpe) {
			log.error(jpe.getMessage());
        }

		CommonModel cm = new CommonModel(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cm);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler({FeignException.class})
    public ResponseEntity feginException(HttpServletRequest req, FeignException e) {
        log.error(req.getRequestURL() + " FeignException: {}", e.getMessage());

        CommonModel cm;
        try {
            JsonNode node = this.objectMapper.readTree(e.contentUTF8());
            cm = new CommonModel(String.valueOf(node.get("code").asInt()), node.get("message").asText());

            return ResponseEntity.status(e.status()).body(cm);
        }
        catch (Exception ex) {
            log.error(ex.getMessage());
            cm = new CommonModel<>(ResponseCodeType.SERVER_ERROR_43001004);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cm);
        }
    }

}
