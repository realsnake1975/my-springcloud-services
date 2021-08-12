package my.springcloud.account.exception;

import my.springcloud.common.constants.CommonConstants;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.logging.*;
import my.springcloud.common.wrapper.CommonModel;
import my.springcloud.config.jackson.JacksonMapperConfig;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j(topic = CommonConstants.LOGGER_NAME)
@RestControllerAdvice(annotations = RestController.class)
@RefreshScope
public class FilterExceptionHandler extends LoggerAspect {

    private final Logger logger = LoggerFactory.getLogger(FilterExceptionHandler.class);

    @Value("${spring.application.name}")
    private String applicationName;

    private static final String UNKNOWN = "00000";

    public FilterExceptionHandler(HttpServletRequest request) {
        super(request, new JacksonMapperConfig().objectMapper());
    }

    @Override
    public void loggingBefore(JoinPoint joinPoint) {
        //
    }

    @Override
    public void loggingSuccess(JoinPoint joinPoint, Object obj) {
        //
    }

    @Override
    public void loggingFail(JoinPoint joinPoint, Exception e) {
        //
    }

    @Override
    public LogDetails getLogDetails(ResponseCodeType responseCodeType, String svcType, String svcClass, String subSvcClass) {
        return LogDetails.builder()
                .sid(NULL)
                .resultCode(responseCodeType.code())
                .startDatetime(LocalDateTime.now())
                .endDatetime(LocalDateTime.now())
                .clientIp(this.extractClientIp())
                .devInfo(DevInfoType.ETC.name())
                .osInfo(this.getOsInfo())
                .applicationName(this.applicationName)
                .devModel(NULL)
                .subNo(NULL)
                .tranId(NULL)
                .svcType(svcType)
                .svcClass(svcClass)
                .subSvcClass(subSvcClass)
                .build();
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
    public ResponseEntity handleMediaTypeException(HttpServletRequest req, HttpMediaTypeNotSupportedException e) {
        logger.error("{} HttpMediaTypeNotSupportedException: {}", req.getRequestURL(), e.getMessage());

        String logString = super.getLogString(this.getLogDetails(ResponseCodeType.SERVER_ERROR_41001003, SvcType.SVC08.name(), SvcClassType.F09.name(), UNKNOWN));
        log.info(logString);

        CommonModel scm = new CommonModel(ResponseCodeType.SERVER_ERROR_41001003);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(scm);
    }

    @SuppressWarnings("rawtypes")
    public void onAuthFail(HttpServletResponse response, ResponseCodeType responseCodeType) throws IOException {
        String logString = super.getLogString(this.getLogDetails(responseCodeType, SvcType.SVC08.name(), SvcClassType.F09.name(), UNKNOWN));
        log.info(logString);

        CommonModel scm = new CommonModel(responseCodeType);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(this.objectMapper.writeValueAsString(scm));
        response.getWriter().flush();
    }

}
