package io.github.agileluo.apidoc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiField {
	
	/** 名字 */
	String name() default "";
	
	/** 字段类型 */
	Type type() default Type.String;

	/** 字段最大长度 */
	int length()

	default 0;

	/** 长度范围 min,max两个值 */
	String range()

	default "";

	/** 枚举值 */
	String[] enumerable() default {};
	
	String dateFormat() default "yyyy-MM-dd HH:mm:ss";
	
	String remark() default "";
	/** 是否属于顶层泛形类   */
	boolean topGenericClass() default false;
	/** 是否显示 */
	boolean show() default true;
	
	enum Type {
		String("字符串"), Boolean("布尔值"), Int("整数"), Float("浮点数"), Array("数组"), Enum("枚举值"), Date("日期"), Url(
				"网址"), Email("邮箱"), Object("对象");
		private String name;

		Type(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
