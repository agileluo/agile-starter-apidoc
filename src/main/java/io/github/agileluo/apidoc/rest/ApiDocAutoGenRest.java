package io.github.agileluo.apidoc.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.agileluo.apidoc.annotation.ApiMethod;
import io.github.agileluo.apidoc.parser.ApiDocClassParser;
import io.github.agileluo.apidoc.vo.ApiDoc;
import io.github.agileluo.apidoc.vo.ErrorCodeDefine;

@RestController
public class ApiDocAutoGenRest {

	@Value("${spring.application.name}")
	private String appName;
	@Autowired
	private ApplicationContext context;
	private ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
	
	private List<ApiDoc> docs = new ArrayList<>();
	private List<ErrorCodeDefine> errors = new ArrayList<>();
	public static int sort = 0;
	
	public void parseDoc() throws Exception {
		List<ApiDoc> result = new ArrayList<>();
		sort = 0;
		
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
		for(Object bean : context.getBeansWithAnnotation(RestController.class).values()){
			String className = bean.getClass().getName();
			if(className.contains("$")){
				className = className.substring(0, className.indexOf("$"));
			}
			Class<?> clazz = Class.forName(className);
			List<ApiDoc> doc = ApiDocClassParser.parse(clazz, appName, nameDiscoverer);
			if(doc != null){
				result.addAll(doc);
			}
		}
		this.docs = result;
	}
	@RequestMapping("/apis")
	@ApiMethod(name="api文档列表", desc="框架自动文档生成接口")
	public  List<ApiDoc>  listDoc() throws Exception{
		parseDoc();
		return docs;
	}
}
