package my.springcloud.common.exception;

@FunctionalInterface
public interface ExceptionFunction<T, R> {

	R apply(T t) throws Exception;

}
