package my.springcloud.common.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CustomLogger {

	SvcType svcType() default SvcType.SVC03;

	SvcClassType svcClassType() default SvcClassType.F07;

	SubSvcClassType subSvcClassType();

}
