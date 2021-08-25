package my.springcloud.common.constants;

public enum ResponseCodeType {

	SUCCESS("2S000000", "성공", "Y"),
	SERVER_ERROR_41001001("41001001", "인증 실패", "N"),
	SERVER_ERROR_41001002("41001002", "권한 없음", "N"),
	SERVER_ERROR_41001003("41001003", "유효하지 않은 method 접근", "N"),
	SERVER_ERROR_41001004("41001004", "유효하지 않는 API 호출", "N"),
	SERVER_ERROR_41001005("41001005", "패스워드 변경 90일 초과", "Y"),
	SERVER_ERROR_41001006("41001006", "차단된 계정", "N"),
	SERVER_ERROR_41001007("41001007", "OTP 5분 초과", "N"),
	SERVER_ERROR_41001008("41001008", "OTP 검증 실패", "N"),
	SERVER_ERROR_41001009("41001009", "잠긴 계정", "N"),
	SERVER_ERROR_41001010("41001010", "SMS 제한 횟수 초과", "N"),
	SERVER_ERROR_41001011("41001011", "로그인 실패 횟수 초과", "N"),
	SERVER_ERROR_41001012("41001012", "이전 패스워드와 동일", "N"),
	SERVER_ERROR_41001013("41001013", "아이디를 포함한 패스워드", "N"),
	SERVER_ERROR_41001014("41001014", "패스워드 정책과 불일치", "N"),
	SERVER_ERROR_41001015("41001015", "존재하지 않는 권한", "N"),
	SERVER_ERROR_42001001("42001001", "필수 파라메터 누락", "N"),
	SERVER_ERROR_42001002("42001002", "조회 데이터 없음", "Y"),
	SERVER_ERROR_42001003("42001003", "데이터 등록 실패", "N"),
	SERVER_ERROR_42001004("42001004", "데이터 수정 실패", "N"),
	SERVER_ERROR_42001005("42001005", "데이터 삭제 실패", "N"),
	SERVER_ERROR_42001006("42001006", "FK 제약으로 인한 데이터 삭제 실패", "N"),
	SERVER_ERROR_42001007("42001007", "파일정보 없음", "N"),
	SERVER_ERROR_42001008("42001008", "유효하지 않은 파일 포멧", "N"),
	SERVER_ERROR_42001009("42001009", "지원하지 않는 데이터 규격", "N"),
	SERVER_ERROR_43001001("43001001", "DB 연동실패", "N"),
	SERVER_ERROR_43001002("43001002", "Redis 연동실패", "N"),
	SERVER_ERROR_43001003("43001003", "카프카 연동 실패", "N"),
	SERVER_ERROR_43001004("43001004", "서버 내부 오류", "N"),
	INF_ERROR_51001001("51001001", "외부시스템 연동 오류", "N");

	private final String code;
	private final String desc;
	private final String successYn;

	ResponseCodeType(String code, String desc, String successYn) {
		this.code = code;
		this.desc = desc;
		this.successYn = successYn;
	}

	public String code() {
		return code;
	}

	public String desc() {
		return desc;
	}

	public String successYn() {
		return successYn;
	}

	@Override
	public String toString() {
		return String.format("code: %s, desc: %s, successYn: %s", code(), desc(), successYn());
	}

}
