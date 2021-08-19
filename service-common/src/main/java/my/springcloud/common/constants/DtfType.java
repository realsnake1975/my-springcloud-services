package my.springcloud.common.constants;

import java.time.format.DateTimeFormatter;

public enum DtfType {

	yyyyMMdd(DateTimeFormatter.ofPattern("yyyyMMdd")),
	yyyyMMddHHmm(DateTimeFormatter.ofPattern("yyyyMMddHHmm")),
	yyyyMMddHHmmss(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
	;

	private final DateTimeFormatter dtf;

	DtfType(DateTimeFormatter dtf) {
		this.dtf = dtf;
	}

	public DateTimeFormatter dtf() {
		return this.dtf;
	}

}
