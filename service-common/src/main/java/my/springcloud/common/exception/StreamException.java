package my.springcloud.common.exception;

import java.util.function.Function;

public class StreamException {

    public static <T, R> Function<T, R> handle(ExceptionFunction<T, R> f) {
        return (T t) -> {
          try {
              return f.apply(t);
          }
          catch (Exception e) {
              throw new RuntimeException(e);
          }
        };
    }

}
