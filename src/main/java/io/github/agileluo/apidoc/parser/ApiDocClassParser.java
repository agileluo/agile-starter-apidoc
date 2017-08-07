package io.github.agileluo.apidoc.parser;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.agileluo.apidoc.annotation.ApiMethod;
import io.github.agileluo.apidoc.annotation.ApiParam;
import io.github.agileluo.apidoc.vo.ApiDoc;
import io.github.agileluo.apidoc.vo.RestParam;



public class ApiDocClassParser {
	
	public static List<ApiDoc> parse(Class<?> clazz, String appName, ParameterNameDiscoverer nameDiscoverer) throws ClassNotFoundException{
		List<ApiDoc> result = new ArrayList<>();
		Method[] methods = clazz.getDeclaredMethods();
		String restUrl = "";
		RequestMapping mapping = clazz.getAnnotation(RequestMapping.class);
		if (mapping != null && mapping.value() != null && mapping.value().length > 0) {
			restUrl = mapping.value()[0];
		}
		for (Method m : methods) {
			RequestMapping methodMapping = m.getAnnotation(RequestMapping.class);
			ApiMethod apiMethod = m.getAnnotation(ApiMethod.class);
			if(methodMapping != null && apiMethod != null){
				ApiDocParser parser = new ApiDocParser();
				parser.setMethod(m);
				parser.setServiceId(appName);
				parser.setRestUrl(restUrl);
				if(apiMethod != null){
					parser.setRequires(apiMethod.requires());
					parser.setName(apiMethod.name());
					parser.setDesc(apiMethod.desc());
					parser.setOpen(apiMethod.open());
				}
				Class<?> gType = null;
				Type actureType = m.getGenericReturnType();
				if (actureType instanceof ParameterizedType) {
					actureType = ((ParameterizedType) actureType).getActualTypeArguments()[0];
					gType = Class.forName(actureType.getTypeName());
					parser.setResultParamType(gType);
				}
				parser.setResult(m.getReturnType());
				RequestMethod[] method = methodMapping.method();
				parser.setReqMethod(method);
				String url = methodMapping.value()[0];
				parser.setUrl(url);
				//公共接口不生成
				if("/apis".equals(restUrl + url)){
					return new ArrayList<>();
				}
				Parameter[] params = m.getParameters();
				String[] names = nameDiscoverer.getParameterNames(m);
				for (int i = 0; i < params.length; i++) {
					Parameter p = params[i];
					String name = names[i];
					RequestBody rbody = p.getAnnotation(RequestBody.class);
					RequestParam rp = p.getAnnotation(RequestParam.class);
					ApiParam ap = p.getAnnotation(ApiParam.class);
					String label = null;
					Integer length = null;
					String remark = null;
					if(rbody != null){
						parser.setJsonBody(true);
					}
					if(rp != null){
						name = rp.value();
					}
					if (ap != null) {
						label = ap.name();
						remark = ap.remark();
						length  = ap.length();
					}
					parser.addParams(new RestParam(p, name,  label, remark, length));
				}
				result.add(parser.genApiDoc());
			}
		}
		return result;
	}
}
