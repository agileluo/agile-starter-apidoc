package io.github.agileluo.apidoc.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMethod;

import io.github.agileluo.apidoc.annotation.ApiField;
import io.github.agileluo.apidoc.rest.ApiDocAutoGenRest;
import io.github.agileluo.apidoc.vo.ApiDoc;
import io.github.agileluo.apidoc.vo.ApiParam;
import io.github.agileluo.apidoc.vo.RestParam;

public class ApiDocParser {

	private String serviceId;
	private String restUrl;
	private String url;
	private RequestMethod[] reqMethod;
	private String name;
	private String desc;
	private String[] requires;
	private boolean open;
	private List<RestParam> params = new ArrayList<>();
	private boolean isJsonBody;
	private Class<?> result;
	private Class<?> resultParamType;
	private Map<Class<?>, String> paramObjMap = new HashMap<>();
	private Method method;

	public ApiDoc genApiDoc() throws ClassNotFoundException {
		ApiDoc api = new ApiDoc();
		api.setServiceId(serviceId);
		String apiUrl = "/" + serviceId + "/" + restUrl + "/" + url;
		apiUrl = apiUrl.replaceAll("///", "/");
		apiUrl = apiUrl.replaceAll("//", "/");
		api.setUrl(apiUrl);
		api.setName(name);
		api.setDescription(desc);
		api.setOpen(open);
		api.setSortNum(ApiDocAutoGenRest.sort++);
		if (isJsonBody) {
			api.setMethod(new String[] { "POST" });
			api.setDataType("json");
		} else {
			if(reqMethod == null || reqMethod.length == 0){
				api.setMethod(new String[]{"GET", "POST"});
			}else{
				String[] methods = new String[reqMethod.length];
				for (int i = 0; i < reqMethod.length; i++) {
					methods[i] = reqMethod[i].name();
				}
				api.setMethod(methods);
			}
			
		}
		Set<String> requireFields = new HashSet<>();
		if (requires != null) {
			requireFields.addAll(Arrays.asList(requires));
		}
		List<ApiParam> apiParams = new ArrayList<>();
		for (RestParam param : params) {
			Parameter pType = param.getType();
			String type = getPrimaryType(pType.getType());
			paramObjMap.put(pType.getType(), param.getName());
			Class<?> gType = getParameType(pType);
			if (type != null) {
				ApiParam apiParam = new ApiParam();
				apiParam.setType(type);
				apiParam.setName(param.getName());
				apiParam.setLabel(param.getLabel());
				apiParam.setRemark(param.getRemark());
				apiParam.setLength(param.getLength());
				if (requireFields.contains(param.getName())) {
					apiParam.setRequire(true);
				}
				if ("Array".equals(type)) {
					if(params.size() == 1){
						apiParam.setName("[]");
					}
					String gTypeInfo = getPrimaryType(gType);
					if(pType.getClass().isArray()){
						gTypeInfo = getPrimaryType(pType.getClass().getComponentType());
					}
					if(gTypeInfo != null){
						apiParam.setType("Array[" + gTypeInfo + "]");
						apiParams.add(apiParam);
					}else{
						apiParams.add(apiParam);
						if(params.size() == 1){
							parseParam("[]", gType, null, null, apiParams, requireFields);
						}else{
							parseParam(param.getName() + "[]", gType, null, null, apiParams, requireFields);
						}
					}
				}else{
					apiParams.add(apiParam);
				}

			} else {
				parseParam(null, pType.getType(), null, gType, apiParams, requireFields);
			}
		}
		api.setParameters(apiParams);
		ApiResultParser resultParser = new ApiResultParser(method);
		api.setResults(resultParser.parser());

		return api;
	}

	private Class<?> getParameType(Parameter pType) throws ClassNotFoundException {
		Class<?> gType = null;
		Type actureType = pType.getParameterizedType();
		if (actureType instanceof ParameterizedType) {
			actureType = ((ParameterizedType) actureType).getActualTypeArguments()[0];
			gType = Class.forName(actureType.getTypeName());
		}
		return gType;
	}
	private Class<?> getParameType(Field f){
		Class<?> result = null;
		if(f.getType().isArray()){
			result = f.getType().getComponentType();
		}else{
			Type pType = f.getGenericType();
			if (pType instanceof ParameterizedType) {
				pType = ((ParameterizedType) pType).getActualTypeArguments()[0];
				try {
					result = Class.forName(pType.getTypeName());
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return result;
	}

	
	
	private void parseParam(String prop, Class<?> type, Field field, Class<?> pType, List<ApiParam> apiParams,
			Set<String> requireFields) {

		for (Field f : ParserCommon.getAllField(type)) {
			ApiField apiField = f.getDeclaredAnnotation(ApiField.class);
			if(apiField != null && apiField.show() == false){
				continue;
			}
			if(Modifier.isStatic(f.getModifiers())){
				continue;
			}
			f.setAccessible(true);
			
			Class<?> fType = f.getType();
			String typeInfo = getPrimaryType(fType);
			String name = prop != null ? prop + "." + f.getName() : f.getName();
			// 数组优化
			if (typeInfo != null) {
				ApiParam apiParam = new ApiParam();
				apiParam.setType(typeInfo);
				apiParam.setName(name);
				if( ApiField.Type.Enum.name().equals(typeInfo)){
					List<String> es = new ArrayList<>();
					for(Enum<?> o : (Enum<?>[])fType.getEnumConstants()){
						es.add(o.name());
					}
					apiParam.setEnumerable(es.toArray(new String[es.size()]));
				}
				
				if (apiField != null) {
					apiParam.setLabel(apiField.name());
					apiParam.setRemark(apiField.remark());
					if(apiField.length() != 0){
						apiParam.setLength(apiField.length());
					}
				}
				if (requireFields.contains(name)) {
					apiParam.setRequire(true);
				}
				apiParams.add(apiParam);
				if ("Array".equals(typeInfo)) {
					pType = getParameType(f);
					String paramClassInfo = getPrimaryType(pType);
					if(paramClassInfo != null){
						apiParam.setParamType(paramClassInfo);
					}else{
						String model = paramObjMap.get(pType);
						if(model != null){
							apiParam.setClazz(model);
						}else{
							paramObjMap.put(pType, name);
							parseParam(name + "[]", pType, null, null, apiParams, requireFields);
						}
					}
				}
			} else {
				ApiParam apiParam = new ApiParam();
				apiParam.setType("Object");
				apiParam.setName(name);
				if (apiField != null) {
					apiParam.setLabel(apiField.name());
					apiParam.setRemark(apiField.remark());
					if(apiField.length() != 0){
						apiParam.setLength(apiField.length());
					}
				}
				apiParams.add(apiParam);
				String model = paramObjMap.get(fType);
				if(model != null){
					apiParam.setClazz(model);
				}else{
					paramObjMap.put(fType, name);
					if (fType == Object.class && pType != null) {
						parseParam(name, pType, f, null, apiParams, requireFields);
					} else {
						parseParam(name, fType, f, null, apiParams, requireFields);
					}
				}
			}
		}

	}

	private String getPrimaryType(Class<?> pType) {
		if(pType == null){
			return null;
		}
		String type = null;
		if (pType == String.class) {
			type = ApiField.Type.String.name();
		} else if (pType == int.class || pType == long.class || pType == Integer.class || pType == Long.class) {
			type = ApiField.Type.Int.name();
		} else if (pType == BigDecimal.class || pType == float.class || pType == double.class || pType == Float.class || pType == Double.class) {
			type = ApiField.Type.Float.name();
		} else if (pType == boolean.class || pType == Boolean.class) {
			type = ApiField.Type.Boolean.name();
		} else if (pType == Date.class || pType == Date.class || pType == java.sql.Date.class || pType == java.sql.Time.class || pType == java.sql.Timestamp.class) {
			type = ApiField.Type.Date.name();
		} else if (pType.isArray() || Collection.class.isAssignableFrom(pType)) {
			type = ApiField.Type.Array.name();
		} else if(pType.isEnum()){
			type = ApiField.Type.Enum.name();
		}
		return type;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getRestUrl() {
		return restUrl;
	}

	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}

	public String[] getRequires() {
		return requires;
	}

	public void setRequires(String[] requires) {
		this.requires = requires;
	}

	public void addParams(RestParam param) {
		this.params.add(param);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public RequestMethod[] getReqMethod() {
		return reqMethod;
	}

	public void setReqMethod(RequestMethod[] reqMethod) {
		this.reqMethod = reqMethod;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public boolean isJsonBody() {
		return isJsonBody;
	}

	public void setJsonBody(boolean isJsonBody) {
		this.isJsonBody = isJsonBody;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<RestParam> getParams() {
		return params;
	}

	public void setParams(List<RestParam> params) {
		this.params = params;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public Class<?> getResult() {
		return result;
	}

	public void setResult(Class<?> result) {
		this.result = result;
	}

	public Class<?> getResultParamType() {
		return resultParamType;
	}

	public void setResultParamType(Class<?> resultParamType) {
		this.resultParamType = resultParamType;
	}
	static class Test{
		List<String> list;
	}
	public static void main(String[] args) throws NoSuchFieldException, SecurityException {
		
	}
}
