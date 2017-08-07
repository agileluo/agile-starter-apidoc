package io.github.agileluo.apidoc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiParam {
	
	/** 名字 */
	String name() default "";

	/** 字段最大长度 */
	int length() default 0;

	/** 枚举值 */
	String[] enumerable() default {};
	
	String remark() default "";
}
