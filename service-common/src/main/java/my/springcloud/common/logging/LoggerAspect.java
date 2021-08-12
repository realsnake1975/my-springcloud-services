package my.springcloud.common.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.springcloud.common.constants.ResponseCodeType;
import my.springcloud.common.utils.TextUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.aspectj.lang.JoinPoint;
import ua_parser.Client;
import ua_parser.Parser;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.util.StringUtils.isEmpty;

public abstract class LoggerAspect {

    protected static final String SEPARATOR = "|";
    protected static final String KV_SEPARATOR = "=";

    protected static final String DFP_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    protected static final String NULL = "null";
    protected static final String DEFAULT_SID = "system";

    protected HttpServletRequest request;
    protected ObjectMapper objectMapper;

    protected LoggerAspect() {

    }

    protected LoggerAspect(HttpServletRequest request) {
        this.request = request;
    }

    protected LoggerAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected LoggerAspect(HttpServletRequest request, ObjectMapper objectMapper) {
        this.request = request;
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings("deprecation")
    protected String extractClientIp() {
        String clientIp = this.request.getHeader("X-Forwarded-For");

        if (isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = this.request.getHeader("Proxy-Client-IP");
        }
        if (isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = this.request.getRemoteAddr();
        }

        if (isEmpty(clientIp)) {
            return NULL;
        }

        return clientIp;
    }

    @SuppressWarnings("deprecation")
    protected String getOsInfo() {
        String userAgent = this.request.getHeader("user-agent");
        if (isEmpty(userAgent)) {
            return NULL;
        }

        Parser parser = new Parser();
        Client client = parser.parse(userAgent);

        if (isEmpty(client.os.family) || isEmpty(client.os.major)) {
            return NULL;
        }

        StringBuilder osInfo = new StringBuilder();
        osInfo.append(client.os.family).append("_").append(client.os.major);
        if (client.os.minor != null) {
            osInfo.append("_").append(client.os.minor);
        }

        return osInfo.toString();
    }

    protected String getLogString(LogDetails logDetails) {
        String logTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DFP_YYYYMMDDHHMMSSSSS));
        String logTime14 = logTime.substring(0, 14);

        StringBuilder sb = new StringBuilder();
        sb.append("SEQ_ID").append(KV_SEPARATOR).append(logTime).append(RandomStringUtils.randomNumeric(4)).append(RandomStringUtils.randomNumeric(4)); // YYYYMMDDHHmmSSsss+{순차 8자리}
        sb.append(SEPARATOR).append("LOG_TIME").append(KV_SEPARATOR).append(logTime14); // 로그를 파일에 Write 시점 시간, YYYYMMDDHHmmSS
        sb.append(SEPARATOR).append("LOG_TYPE").append(KV_SEPARATOR).append("SVC"); // 3자리, SVC
        sb.append(SEPARATOR).append("SID").append(KV_SEPARATOR).append(logDetails.getSid()); // "CTN : 0100xxxxxxxx(12자리), ID : alphanumeric (50바이트)"
        sb.append(SEPARATOR).append("RESULT_CODE").append(KV_SEPARATOR).append(logDetails.getResultCode()); // 8자리, "1xxxxxxx : 정보, 2xxxxxxx : 성공, 3xxxxxxx : 클라이언트 에러, 4xxxxxxx : 서버에러(서비스), 5xxxxxxx : 서버에러(연동)"
        sb.append(SEPARATOR).append("REQ_TIME").append(KV_SEPARATOR).append(logDetails.getStartDatetime().format(DateTimeFormatter.ofPattern(DFP_YYYYMMDDHHMMSSSSS))); // YYYYMMDDHHmmSSsss
        sb.append(SEPARATOR).append("RSP_TIME").append(KV_SEPARATOR).append(logDetails.getEndDatetime().format(DateTimeFormatter.ofPattern(DFP_YYYYMMDDHHMMSSSSS))); // YYYYMMDDHHmmSSsss
        sb.append(SEPARATOR).append("CLIENT_IP").append(KV_SEPARATOR).append(logDetails.getClientIp()); // 클라이언트 IP
        sb.append(SEPARATOR).append("DEV_INFO").append(KV_SEPARATOR).append(logDetails.getDevInfo()); // 5자리, 접속 단말 타입(TV, STB)
        sb.append(SEPARATOR).append("OS_INFO").append(KV_SEPARATOR).append(logDetails.getOsInfo()); // OS 정보
        sb.append(SEPARATOR).append("NW_INFO").append(KV_SEPARATOR).append("ETC"); // 접속 네트워크 정보
        sb.append(SEPARATOR).append("SVC_NAME").append(KV_SEPARATOR).append(logDetails.getApplicationName()); // 32자리, 각 서비스/시스템 명
        sb.append(SEPARATOR).append("DEV_MODEL").append(KV_SEPARATOR).append(logDetails.getDevModel()); // 50자리, 단말 모델명, 셋톱박스 common 에서 추출
        sb.append(SEPARATOR).append("CARRIER_TYPE").append(KV_SEPARATOR).append("L"); // 1자리, 통신사 구분, L (LGU+) E (etc)
        sb.append(SEPARATOR).append("SVC_TYPE").append(KV_SEPARATOR).append(logDetails.getSvcType());
        sb.append(SEPARATOR).append("SUB_NO").append(KV_SEPARATOR).append(logDetails.getSubNo());
        sb.append(SEPARATOR).append("TRAN_ID").append(KV_SEPARATOR).append(logDetails.getTranId());
        sb.append(SEPARATOR).append("SVC_CLASS").append(KV_SEPARATOR).append(logDetails.getSvcClass());
        sb.append(SEPARATOR).append("SUB_SVC_CLASS").append(KV_SEPARATOR).append(logDetails.getSubSvcClass());
        sb.append(SEPARATOR).append("EXT_MSG_ID").append(KV_SEPARATOR).append(
                TextUtils.isNotEmpty(logDetails.getExtMsgId()) ? logDetails.getExtMsgId() : NULL
        );
        sb.append(SEPARATOR).append("EXT_RESULT_CODE").append(KV_SEPARATOR).append(
                TextUtils.isNotEmpty(logDetails.getExtResultCode()) ? logDetails.getExtResultCode() : NULL
        );
        sb.append(SEPARATOR).append("R1").append(KV_SEPARATOR).append(
                TextUtils.isNotEmpty(logDetails.getR1()) ? logDetails.getR1() : NULL
        );
        sb.append(SEPARATOR).append("R2").append(KV_SEPARATOR).append(
                TextUtils.isNotEmpty(logDetails.getR2()) ? logDetails.getR2() : NULL
        );
        sb.append(SEPARATOR).append("R3").append(KV_SEPARATOR).append(
                TextUtils.isNotEmpty(logDetails.getR3()) ? logDetails.getR3() : NULL
        );

        return sb.toString();
    }

    // 카프카 이벤트수신 시 request 적용되지 않음
    protected void beforeLogging() {
        this.request.setAttribute("x-start-datetime", LocalDateTime.now());
        this.request.setAttribute("x-client-ip", this.extractClientIp());
        this.request.setAttribute("x-os-info", this.getOsInfo());

        String tranId = this.request.getHeader("TRAN_ID");
        if (tranId == null) {
            tranId = NULL;
        }

        this.request.setAttribute("x-tran-id", tranId);
    }

    public abstract void loggingBefore(JoinPoint joinPoint);
    public abstract void loggingSuccess(JoinPoint joinPoint, Object obj);
    public abstract void loggingFail(JoinPoint joinPoint, Exception e);
    public abstract LogDetails getLogDetails(ResponseCodeType responseCodeType, String svcType, String svcClass, String subSvcClass);

}
