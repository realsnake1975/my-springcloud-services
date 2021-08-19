package my.springcloud.common.constants;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum AccountStatusType {

	APPROVAL("approval", "승인", true, true, 1),
	BLOCK("block", "차단", true, false, 2),
	LOCKED("lock", "잠김", false, false, 3),
	;

	private final String code;
	private final String desc;
	private final boolean openYn;
	private final boolean defaultConfigYn;
	private final int sortOrder;

	AccountStatusType(String code, String desc, boolean openYn, boolean defaultConfigYn, int sortOrder) {
		this.code = code;
		this.desc = desc;
		this.openYn = openYn;
		this.defaultConfigYn = defaultConfigYn;
		this.sortOrder = sortOrder;
	}

	public static List<AccountStatusType> sortedValues() {
		return Arrays.stream(values())
			.filter(e -> e.openYn())
			.sorted(Comparator.comparingInt(e -> e.sortOrder()))
			.collect(Collectors.toList());
	}

	public String code() {
		return code;
	}

	public String desc() {
		return desc;
	}

	public boolean openYn() {
		return openYn;
	}

	public boolean defaultConfigYn() {
		return defaultConfigYn;
	}

	public int sortOrder() {
		return sortOrder;
	}

	@Override
	public String toString() {
		return String.format("code:%s, desc:%s, openYn:%s, defaultConfigYn:%s, sortOrder:%s", code(), desc(), openYn(),
			defaultConfigYn(), sortOrder());
	}

}
