package io.github.agileluo.apidoc.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.agileluo.apidoc.annotation.ApiField;
import io.github.agileluo.apidoc.vo.ApiFieldType;
import io.github.agileluo.apidoc.vo.ApiResult;
import io.github.agileluo.apidoc.vo.FieldType;

public class ApiResultParser extends ParserCommon {
	
	private Method method;
	private List<ApiResult> results;
	private Class<?> genericType;
	private Map<Class<?>, String> clazzMap = new HashMap<>();
	
	
	public ApiResultParser(Method method) {
		super();
		this.method = method;
	}

	public List<ApiResult> parser() {
		results = new ArrayList<>();
		parserResult(null, method.getReturnType(), null);
		return results;
	}
	
	@Override
	public Class<?> getMethodGenericType() {
		return genericType;
	}

	void parserResult(String prop, Class<?> clazz, Field field) {
		if (clazz == Void.class || clazz == void.class) {
			return;
		}
		if(prop == null){
			Type actureType = method.getGenericReturnType();
			if (actureType instanceof ParameterizedType) {
				actureType = ((ParameterizedType) actureType).getActualTypeArguments()[0];
				try {
					genericType = Class.forName(actureType.getTypeName());
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
			boolean isPrimaryClazz = isPrimaryClass(clazz, genericType);
			String classType = classToStr(clazz);
			String paramType = classToStr(genericType);
			ApiResult apiResult = new ApiResult();
			apiResult.setName(null);
			apiResult.setType(classType);
			apiResult.setParamType(paramType);
			apiResult.setEnumerable(getEnumList(clazz));
			results.add(apiResult);
			if(!isPrimaryClazz){
				if(FieldType.Object.equals(classType)){
					clazzMap.put(clazz, "ROOT");
					for (Field f : getAllField(clazz)) {
						if(notShow(f)){
							continue;
						}
						parserResult(f.getName(), null, f);
					}
				}else if(FieldType.Array.equals(classType)){
					if(genericType != null){
						parserResult("[]", genericType, null);
					}
				}
			}
			return ;
		}
		if(clazz != null){
			for (Field f : getAllField(clazz)) {
				if(notShow(f)){
					continue;
				}
				String name = prop != null ? prop + "." + f.getName() : f.getName();
				parserResult(name, null, f);
			}
		}else{
			ApiFieldType typeInfo = getTypeInfo(field);
			String name = prop;
			ApiResult apiResult = new ApiResult();
			apiResult.setName(name);
			apiResult.setType(typeInfo.getType());
			apiResult.setLabel(typeInfo.getLabel());
			apiResult.setRemark(typeInfo.getRemark());
			apiResult.setEnumerable(typeInfo.getEnumerable());
			results.add(apiResult);
			
			boolean isPrimaryClazz = isPrimaryClass(typeInfo.getClazz(), typeInfo.getGenericType());
			if(isPrimaryClazz){
				String paramType = classToStr(typeInfo.getGenericType());
				apiResult.setParamType(paramType);
			}else{
				Class<?> deapClass = null;
				String existNode = null;
				String deapName = name;
				if(FieldType.Object.equals(typeInfo.getType())){
					deapClass = typeInfo.getClazz();
					if(deapClass == Object.class && typeInfo.isTopGenericClass()){
						deapClass = genericType;
					}
				}else if(FieldType.Array.equals(typeInfo.getType())){
					deapClass = typeInfo.getGenericType();
					deapName = name + "[]";
				}
				
				existNode = clazzMap.get(deapClass);
				if(existNode != null){
					apiResult.setClazz(existNode);
				}else{
					clazzMap.put(deapClass, name);
					if(deapClass != null){
						parserResult(deapName, deapClass, null);
					}
				}
			}
		}
	}
	public boolean notShow(Field f){
		if(Modifier.isStatic(f.getModifiers())){
			return true;
		}
		ApiField apiField = f.getDeclaredAnnotation(ApiField.class);
		if(apiField != null && apiField.show() == false){
			return true;
		}
		return false;
	}
}
