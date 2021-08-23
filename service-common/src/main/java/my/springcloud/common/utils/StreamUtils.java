package my.springcloud.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import my.springcloud.common.exception.ExceptionFunction;

public class StreamUtils {

	private StreamUtils() {
		throw new IllegalStateException("Utility class");
	}

	// 중복 제거
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public static <T, R> Function<T, R> wrap(ExceptionFunction<T, R> ef) {
		return (T t) -> {
			try {
				return ef.apply(t);
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

}
