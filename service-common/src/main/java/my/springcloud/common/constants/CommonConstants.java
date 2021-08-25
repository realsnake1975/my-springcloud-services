package my.springcloud.common.constants;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * 홈쇼핑모아 상수들
 */
public interface CommonConstants {

	// 기본 페이지 사이즈
	int DEFAULT_PAGE_SIZE = 10;

	// 기본 페이지 블록 사이즈
	int DEFAULT_PAGE_BLOCK_SIZE = 10;

	// 기본 문자 인코딩: UTF-8
	String DEFAULT_ENCODING = "UTF-8";

	// 기본 날짜형식: yyyy-MM-dd HH:mm:ss
	String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	// 기본 날짜형식: yyyy-MM-dd HH:mm
	String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

	// 짧은 날짜형식: yyyyMMdd
	String SHORT_DATE_FORMAT = "yyyyMMdd";

	// 연월 날짜형식: yyyyMM
	String YYYYMM_FORMAT = "yyyyMM";

	// 연월 날짜형식: yyyy-MM
	String YYYY_MM_FORMAT = "yyyy-MM";

	// 연월일 날짜형식: yyyy-MM-dd
	String YYYY_MM_DD_FORMAT = "yyyy-MM-dd";

	// 연 형식: yyyy
	String YYYY_FORMAT = "yyyy";

	// 기본 버퍼 사이즈: 4096
	int DEFAULT_BUFFER_SIZE = 4096;

	// 소수점 자리수 표시: 00,000
	DecimalFormat DF_FORMAT = new DecimalFormat("#,###");

	String ZERO_DATE = "0000-00-00";

	// Signing key for HS512 algorithm, You can use the page http://www.allkeysgenerator.com/ to generate all kinds of keys
	String JWT_SECRET = "Yq3t6w9z$C&F)H@McQfTjWnZr4u7x!A%D*G-KaNdRgUkXp2s5v8y/B?E(H+MbQeS";

	String AUTH_LOGIN_URL = "/v1/auth";

	// JWT token Header
	String TOKEN_HEADER = "Authorization";

	String RECONFIRM_TOKEN_HEADER = "Reconfirm-Token";

	String TOKEN_PREFIX = "Bearer ";

	String TOKEN_TYPE = "JWT";

	String TOKEN_ISSUER = "realsnake";

	String TOKEN_SUBJECT = "my-members";

	// 허용하는 이미지 파일의 확장자
	List<String> ALLOWED_IMAGE_EXT = Arrays.asList("png", "jpeg", "jpg", "gif");
	// 허용하는 파일의 확장자
	List<String> ALLOWED_FILE_EXT = Arrays.asList("png", "jpeg", "jpg", "gif", "pdf", "hwp", "doc", "docx", "xls", "xlsx", "ppt", "pptx");

	List<String> ALLOWED_IMAGE_MIME_TYPES = Arrays.asList("image/gif", "image/jpeg", "image/jpg", "image/png");

	List<String> ALLOWED_FILE_MIME_TYPES = Arrays.asList(
		"image/gif",
		"image/jpeg",
		"image/jpg",
		"image/png",
		"application/pdf",
		"application/x-hwp",
		"applicaion/haansofthwp",
		"applicaion/vnd.hancom",
		"application/octet-stream",
		"application/msword",
		"application/vnd.ms-word",
		"application/vnd.ms-excel",
		"application/vnd.ms-powerpoint",
		"application/vnd.openxmlformats-officedocument",
		"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
		"application/vnd.openxmlformats-officedocument.presentationml.presentation"
	);

	// 10M
	long DEFAULT_UPLOAD_SIZE = 10L * 1024 * 1024;

	String LOGGER_NAME = "MY-LOGGER";

}
