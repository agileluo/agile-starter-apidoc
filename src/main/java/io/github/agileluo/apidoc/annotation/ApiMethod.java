package io.github.agileluo.apidoc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * api文档自动生成注解
 * @author luoml
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ApiMethod {
	/** 名称  */
	String name();
	/** 描述 */
	String desc() default "";
	/** 是否为对外开放接口 */
	boolean open() default false;
	/** 非空字段 */
	String[] requires() default {};
}
