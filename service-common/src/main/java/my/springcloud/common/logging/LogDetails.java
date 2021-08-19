package my.springcloud.common.logging;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class LogDetails implements Serializable {

	private final String resultCode;
	private final LocalDateTime startDatetime;
	private final LocalDateTime endDatetime;
	private final String clientIp;
	private final String devInfo;
	private final String osInfo;
	private final String applicationName;
	private final String devModel;
	private final String tranId;
	private final String svcType;
	private final String svcClass;
	private final String subSvcClass;
	@Setter
	private String sid;
	@Setter
	private String subNo;
	// 외부연동 메시지ID
	@Setter
	private String extMsgId;
	// 외부연동 결과 코드
	@Setter
	private String extResultCode;

	@Setter
	private String r1;
	@Setter
	private String r2;
	@Setter
	private String r3;

	@Builder
	public LogDetails(String sid, String resultCode, LocalDateTime startDatetime, LocalDateTime endDatetime,
		String clientIp, String devInfo, String osInfo, String applicationName, String devModel, String subNo,
		String tranId, String svcType, String svcClass, String subSvcClass) {
		this.sid = sid;
		this.resultCode = resultCode;
		this.startDatetime = startDatetime;
		this.endDatetime = endDatetime;
		this.clientIp = clientIp;
		this.devInfo = devInfo;
		this.osInfo = osInfo;
		this.applicationName = applicationName;
		this.devModel = devModel;
		this.subNo = subNo;
		this.tranId = tranId;
		this.svcType = svcType;
		this.svcClass = svcClass;
		this.subSvcClass = subSvcClass;
	}

	@Builder
	public LogDetails(String sid, String resultCode, LocalDateTime startDatetime, LocalDateTime endDatetime,
		String clientIp, String devInfo, String osInfo, String applicationName, String devModel, String subNo,
		String tranId, String svcType, String svcClass, String subSvcClass, String extMsgId, String extResultCode) {
		this(sid, resultCode, startDatetime, endDatetime, clientIp, devInfo, osInfo, applicationName, devModel, subNo,
			tranId, svcType, svcClass, subSvcClass);
		this.extMsgId = extMsgId;
		this.extResultCode = extResultCode;
	}

	@Builder
	public LogDetails(String sid, String resultCode, LocalDateTime startDatetime, LocalDateTime endDatetime,
		String clientIp, String devInfo, String osInfo, String applicationName, String devModel, String subNo,
		String tranId, String svcType, String svcClass, String subSvcClass, String extMsgId, String extResultCode,
		String r1, String r2, String r3) {
		this(sid, resultCode, startDatetime, endDatetime, clientIp, devInfo, osInfo, applicationName, devModel, subNo,
			tranId, svcType, svcClass, subSvcClass, extMsgId, extResultCode);
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
	}

}
