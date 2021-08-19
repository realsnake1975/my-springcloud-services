package my.springcloud.common.logging;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.common.constants.CommonConstants;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.exception.ServiceException;
import my.springcloud.common.model.auth.LoginCheck;
import my.springcloud.common.sec.model.CustomUserDetails;

@Slf4j(topic = CommonConstants.LOGGER_NAME)
@Component
@Aspect
public class CustomLoggerAspect extends LoggerAspect {

	@Value("${spring.application.name}")
	private String applicationName;

	public CustomLoggerAspect(HttpServletRequest request, ObjectMapper objectMapper) {
		super(request, objectMapper);
	}

	private CustomLogger getLoggerAnnotation(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		return method.getAnnotation(CustomLogger.class);
	}

	@Before("@annotation(CustomLogger)")
	@Override
	public void loggingBefore(JoinPoint joinPoint) {
		super.beforeLogging();
	}

	@AfterReturning(value = "@annotation(CustomLogger)", returning = "obj")
	@Override
	public void loggingSuccess(JoinPoint joinPoint, Object obj) {
		CustomLogger cl = this.getLoggerAnnotation(joinPoint);
		String logString = super.getLogString(
			this.getLogDetails(ResponseCodeType.SUCCESS, cl.svcType().name(), cl.svcClassType().name(),
				cl.subSvcClassType().name()));
		log.info(logString);
	}

	@AfterThrowing(value = "@annotation(CustomLogger)", throwing = "e")
	@Override
	public void loggingFail(JoinPoint joinPoint, Exception e) {
		ResponseCodeType responseCodeType;
		if (e instanceof ServiceException) {
			responseCodeType = ((ServiceException)e).getResponseCodeType();
		} else {
			responseCodeType = ResponseCodeType.SERVER_ERROR_43001004;
		}

		CustomLogger cl = this.getLoggerAnnotation(joinPoint);
		String logString = super.getLogString(
			this.getLogDetails(responseCodeType, cl.svcType().name(), cl.svcClassType().name(),
				cl.subSvcClassType().name()));
		log.info(logString);
	}

	@SneakyThrows
	@Override
	public LogDetails getLogDetails(ResponseCodeType responseCodeType, String svcType, String svcClass,
		String subSvcClass) {
		if ("EventSubService".equalsIgnoreCase(this.applicationName)) {
			this.applicationName = "BroadcastProductQueryService";
		}

		String loginId = NULL;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (Objects.nonNull(auth)) {
			if (auth instanceof UsernamePasswordAuthenticationToken) {
				CustomUserDetails admin = (CustomUserDetails)auth.getPrincipal();
				loginId = admin.getUsername();
			} else if (auth instanceof AnonymousAuthenticationToken) {
				try {
					LoginCheck loginCheck = this.objectMapper.readValue(request.getInputStream(), LoginCheck.class);
					loginId = loginCheck.getUsername();
				} catch (Exception ignored) {
					//
				}
			}
		}

		return LogDetails.builder()
			.sid(loginId)
			.resultCode(responseCodeType.code())
			.startDatetime((LocalDateTime)super.request.getAttribute("x-start-datetime"))
			.endDatetime(LocalDateTime.now())
			.clientIp((String)super.request.getAttribute("x-client-ip"))
			.devInfo(DevInfoType.ETC.name())
			.osInfo((String)super.request.getAttribute("x-os-info"))
			.applicationName(this.applicationName)
			.devModel(NULL)
			.subNo(NULL)
			.tranId((String)super.request.getAttribute("x-tran-id"))
			.svcType(svcType)
			.svcClass(svcClass)
			.subSvcClass(subSvcClass)
			.build();
	}

}
