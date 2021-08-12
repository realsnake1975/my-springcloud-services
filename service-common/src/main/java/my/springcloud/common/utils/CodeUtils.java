package my.springcloud.common.utils;

import my.springcloud.common.constants.*;
import my.springcloud.common.model.dto.CodeDto;

import java.util.List;
import java.util.stream.Collectors;

public class CodeUtils {

    private CodeUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static List<CodeDto> getAccountStatusType() {
        return AccountStatusType.sortedValues().stream()
                .map(e -> new CodeDto(e.code(), e.desc(), e.defaultConfigYn()))
                .collect(Collectors.toList());
    }

}
