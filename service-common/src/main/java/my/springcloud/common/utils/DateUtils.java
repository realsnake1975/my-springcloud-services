package my.springcloud.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

public class DateUtils {

	private DateUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * 시작일자로 부터 종료일자 까지의 데이트 일자 맵을 반환한다.
	 *
	 * @param startDt
	 * @param endDt
	 * @return
	 */
	public static Map<String, Object> getBetweenDateMap(String startDt, String endDt) {
		LocalDate startDate = LocalDate.parse(startDt);
		LocalDate endDate = LocalDate.parse(endDt);

		Map<String, Object> map = new LinkedHashMap<>();

		while (!startDate.isAfter(endDate)) {
			map.put(startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), null);
			startDate = startDate.plusDays(1L);
		}

		return map;
	}

	/**
	 * 두 날짜 사이의 일수를 계산합니다.
	 *
	 * @param startDt
	 * @param endDt
	 * @return
	 */
	public static long getBetweenDays(String startDt, String endDt) {
		LocalDate startDate = LocalDate.parse(startDt);
		LocalDate endDate = LocalDate.parse(endDt);

		return ChronoUnit.DAYS.between(startDate, endDate);
	}

	/**
	 * 날짜+시간 문자열에서 시간값만 변경하여 준다.
	 *
	 * @param date
	 * @param replacedTime
	 * @return
	 */
	public static String replaceTime(String date, String replacedTime) {
		return date.replaceAll("([0-9]{2}):([0-9]{2}):([0-9]{2})", replacedTime);
	}

	/**
	 * timestamp 필드에 대하여 두가지 포맷을 받을 수 있도록 한다. (yyyyMMddHHmmssSSS, timestamp 값)
	 *
	 * @param timestamp
	 * @return
	 */
	public static String convertTimestampToString(String timestamp) {
		// timestamp 필드 연동 기본포맷
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

		try {
			return convertTimestampToString(dateFormat.parse(timestamp));
		} catch (ParseException e) {
			return convertTimestampToString(new Date(Long.parseLong(timestamp)));
		}
	}

	/**
	 * 현재 시간으로부터 시간이 얼마나 지났는지 메시지 타임에 대한 포맷을 문자열로 반환한다.
	 *
	 * @param targetDt
	 * @return
	 */
	public static String getMessageTimeFromNow(LocalDateTime targetDt) {
		LocalDateTime now = LocalDateTime.now();
		if (targetDt == null) {
			targetDt = now;
		}

		long diff = targetDt.until(now, ChronoUnit.MINUTES);

		if (diff <= 0) {
			return "방금 전";
		} else if (diff < 60) {
			return String.format("%d분 전", diff);
		}

		long hour = diff / 60;
		if (hour < 24) {
			return String.format("%d시간 전", hour);
		}

		long day = hour / 24;
		if (day < 7) {
			return String.format("%d일 전", day);
		}

		long week = day / 7;
		if (week < 4) {
			return String.format("%d주 전", week);
		}

		long month = week / 4;
		if (month < 12) {
			return String.format("%d달 전", month);
		}

		long year = month / 12;

		return String.format("%d년 전", year);
	}

	private static String convertTimestampToString(Date utc) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

		return dateFormat.format(utc);
	}

	/**
	 * 문자열 날짜 형식이 주어진 날짜 형식이 맞는지 확인한다.
	 *
	 * @param localDateTime
	 * @param dateFormatter
	 * @return
	 */
	public static Boolean validateLocalDateTimeString(String localDateTime, DateTimeFormatter dateFormatter) {
		try {
			LocalDate.parse(localDateTime, dateFormatter);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}

	/**
	 * yyyyMMddHHmm 형식의 시작시간과 종료시간이 주어졌을 때 역전되어있진 않은지 체크한다.
	 * 역전이 아니라면 기존 종료일자
	 * 역전이라면 +1일 종료일자
	 *
	 * @param startDt
	 * @param endDt
	 * @return endDt
	 */
	public static String checkDateInversion(String startDt, String endDt) {
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

		LocalDateTime start = LocalDateTime.parse(startDt, dateFormat);
		LocalDateTime end = LocalDateTime.parse(endDt, dateFormat);

		if (end.isBefore(start)) {
			end = end.plusDays(1);
		}

		return end.format(dateFormat);
	}

}
