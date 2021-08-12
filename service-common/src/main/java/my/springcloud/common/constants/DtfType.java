package my.springcloud.common.constants;

import java.time.format.DateTimeFormatter;

public enum DtfType {

    yyyyMMdd(DateTimeFormatter.ofPattern("yyyyMMdd")),
    yyyyMMddHHmm(DateTimeFormatter.ofPattern("yyyyMMddHHmm")),
    yyyyMMddHHmmss(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
    ;

    DtfType(DateTimeFormatter dtf) {
        this.dtf = dtf;
    }

    private final DateTimeFormatter dtf;

    public DateTimeFormatter dtf() {
        return this.dtf;
    }

}
