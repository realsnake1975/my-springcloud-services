package my.springcloud.common.utils;

import java.util.List;
import java.util.stream.Collectors;

import my.springcloud.common.constants.AccountStatusType;
import my.springcloud.common.model.CodeDetail;

public class CodeUtils {

	private CodeUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static List<CodeDetail> getAccountStatusType() {
		return AccountStatusType.sortedValues().stream()
			.map(e -> new CodeDetail(e.code(), e.desc(), e.defaultConfigYn()))
			.collect(Collectors.toList());
	}

}
