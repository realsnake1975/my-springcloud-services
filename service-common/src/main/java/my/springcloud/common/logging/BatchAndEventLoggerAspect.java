package my.springcloud.common.logging;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.common.constants.CommonConstants;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.exception.ServiceException;
import my.springcloud.common.exception.SettopboxException;

@Slf4j(topic = CommonConstants.LOGGER_NAME)
@Component
@Aspect
public class BatchAndEventLoggerAspect extends LoggerAspect {

	AtomicReference<LocalDateTime> arStartDatetime = new AtomicReference<>();
	@Value("${spring.application.name}")
	private String applicationName;

	private BatchAndEventLogger getLoggerAnnotation(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		return method.getAnnotation(BatchAndEventLogger.class);
	}

	@Before("@annotation(BatchAndEventLogger)")
	@Override
	public void loggingBefore(JoinPoint joinPoint) {
		arStartDatetime.set(LocalDateTime.now());
	}

	@AfterReturning(value = "@annotation(BatchAndEventLogger)", returning = "obj")
	@Override
	public void loggingSuccess(JoinPoint joinPoint, Object obj) {
		BatchAndEventLogger bl = this.getLoggerAnnotation(joinPoint);
		String logString = super.getLogString(
			this.getLogDetails(ResponseCodeType.SUCCESS, bl.svcType().name(), bl.svcClassType().name(),
				bl.subSvcClassType().name()));
		log.info(logString);
	}

	@AfterThrowing(value = "@annotation(BatchAndEventLogger)", throwing = "e")
	@Override
	public void loggingFail(JoinPoint joinPoint, Exception e) {
		ResponseCodeType responseCodeType;
		if (e instanceof ServiceException) {
			responseCodeType = ((ServiceException)e).getResponseCodeType();
		} else if (e instanceof SettopboxException) {
			responseCodeType = ((SettopboxException)e).getResponseCodeType();
		} else {
			responseCodeType = ResponseCodeType.SERVER_ERROR_43001004;
		}

		BatchAndEventLogger bl = this.getLoggerAnnotation(joinPoint);
		String logString = super.getLogString(
			this.getLogDetails(responseCodeType, bl.svcType().name(), bl.svcClassType().name(),
				bl.subSvcClassType().name()));
		log.info(logString);
	}

	@SneakyThrows
	@Override
	public LogDetails getLogDetails(ResponseCodeType responseCodeType, String svcType, String svcClass,
		String subSvcClass) {
		if ("EventSubService".equalsIgnoreCase(this.applicationName)) {
			this.applicationName = "BroadcastProductQueryService";
		}

		return LogDetails.builder()
			.sid(DEFAULT_SID)
			.resultCode(responseCodeType.code())
			.startDatetime(Optional.ofNullable(arStartDatetime.getAndSet(null)).orElse(LocalDateTime.now()))
			.endDatetime(LocalDateTime.now())
			.clientIp(NULL)
			.devInfo(DevInfoType.ETC.name())
			.osInfo(NULL)
			.applicationName(this.applicationName)
			.devModel(NULL)
			.subNo(NULL)
			.tranId(NULL)
			.svcType(svcType)
			.svcClass(svcClass)
			.subSvcClass(subSvcClass)
			.build();
	}

}
