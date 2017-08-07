package io.github.agileluo.apidoc.parser;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.agileluo.apidoc.annotation.ApiField;
import io.github.agileluo.apidoc.vo.ApiFieldType;
import io.github.agileluo.apidoc.vo.FieldType;

public class ParserCommon {
	
	private static Logger log = LoggerFactory.getLogger(ParserCommon.class);
	
	public Class<?> getMethodGenericType(){
		return null;
	}
	
	public Class<?> getGenericType(Type type){
		Class<?> gType = null;
		if (type instanceof ParameterizedType) {
			type = ((ParameterizedType) type).getActualTypeArguments()[0];
			try {
				if("T".equals(type.getTypeName())){
					return getMethodGenericType();
				}
				gType = Class.forName(type.getTypeName());
			} catch (ClassNotFoundException e) {
				log.error("解析泛型异常", e);
			}
		}
		return gType;
	}

	public String classToStr(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		String type = FieldType.Object;
		if (clazz == String.class) {
			type = FieldType.String;
		} else if (clazz == int.class || clazz == long.class || clazz == Integer.class || clazz == Long.class) {
			type = FieldType.Int;
		} else if (clazz == BigDecimal.class || clazz == float.class || clazz == double.class || clazz == Float.class
				|| clazz == Double.class) {
			type = FieldType.Float;
		} else if (clazz == boolean.class || clazz == Boolean.class) {
			type = FieldType.Boolean;
		} else if (clazz == Date.class || clazz == java.sql.Date.class || clazz == java.sql.Time.class || clazz == java.sql.Timestamp.class) {
			type = FieldType.Date;
		} else if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)) {
			type = FieldType.Array;
		} else if (clazz.isEnum()) {
			type = FieldType.Enum;
		}
		return type;
	}
	
	public ApiFieldType getTypeInfo(Field field) {
		ApiFieldType result = new ApiFieldType();
		Class<?> clazz = field.getType();
		result.setName(field.getName());
		result.setClazz(clazz);
		String type = classToStr(clazz);
		result.setEnumerable(getEnumList(clazz));
		result.setType(type);
		result.setGenericType(getGenericType(field));
		ApiField apiField = field.getAnnotation(ApiField.class);
		if (apiField != null) {
			result.setLabel(apiField.name());
			result.setTopGenericClass(apiField.topGenericClass());
			result.setRemark(apiField.remark());
		}
		return result;
	}
	public List<String> getEnumList(Class<?> clazz){
		if(clazz == null || !clazz.isEnum()){
			return null;
		}
		List<String> result = new ArrayList<>();
		for (Enum<?> o : (Enum<?>[]) clazz.getEnumConstants()) {
			result.add(o.name());
		}
		return result;
	}
	
	private static Set<Class<?>> primaryClass = new HashSet<>();
	static {
		primaryClass.add(String.class);
		primaryClass.add(int.class);
		primaryClass.add(Integer.class);
		primaryClass.add(long.class);
		primaryClass.add(Long.class);
		primaryClass.add(BigDecimal.class);
		primaryClass.add(float.class);
		primaryClass.add(Float.class);
		primaryClass.add(double.class);
		primaryClass.add(Double.class);
		primaryClass.add(boolean.class);
		primaryClass.add(Boolean.class);
		primaryClass.add(Date.class);
		primaryClass.add(java.sql.Date.class);
		primaryClass.add(java.sql.Time.class);
		primaryClass.add(java.sql.Timestamp.class);
	}

	public boolean isPrimaryClass(Class<?> clazz, Class<?> genericType) {
		if(clazz == null){
			return false;
		}
		if (primaryClass.contains(clazz)) {
			return true;
		}
		if (clazz.isArray()) {
			return isPrimaryClass(clazz.getComponentType(), null);
		}
		if(Collection.class.isAssignableFrom(clazz)){
			if(genericType == null){
				return isPrimaryClass(getMethodGenericType(), null);
			}else{
				return isPrimaryClass(genericType, null);
			}
		}
		if (clazz.isEnum()) {
			return true;
		}
		return false;
	}
	public Class<?> getGenericType(Field field) {
		Class<?> result = null;
		if (field.getType().isArray()) {
			result = field.getType().getComponentType();
		} else {
			result = getGenericType(field.getGenericType());
		}
		return result;
	}
	
	public static List<Field> getAllField(Class<?> clazz){
		List<Field> result = new ArrayList<>();
		if(clazz == null){
			return result;
		}
		List<Class<?>> clazzs = new ArrayList<>();
		for(; clazz != Object.class && clazz != null; clazz = clazz.getSuperclass()){
			clazzs.add(clazz);
		}
		for(int i = clazzs.size() -1 ; i >= 0; i--){
			Class<?> c = clazzs.get(i);
			result.addAll(Arrays.asList(c.getDeclaredFields()));
		}
		return result;
	}
	
}
