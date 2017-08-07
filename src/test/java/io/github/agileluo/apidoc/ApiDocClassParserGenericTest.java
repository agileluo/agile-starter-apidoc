package io.github.agileluo.apidoc;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.github.agileluo.apidoc.annotation.ApiField;
import io.github.agileluo.apidoc.annotation.ApiMethod;
import io.github.agileluo.apidoc.parser.ApiDocClassParser;
import io.github.agileluo.apidoc.vo.ApiDoc;
import io.github.agileluo.apidoc.vo.ApiResult;

public class ApiDocClassParserGenericTest {
	
	private String appName = "testRest";
	private ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
	
	List<ApiDoc> docs;
	private ApiDoc doc;
	
	@Before
	public void before() throws ClassNotFoundException{
		docs = ApiDocClassParser.parse(SimpleGerricRest.class, appName, nameDiscoverer);
		doc = docs.get(0);
	}
	
	@Test
	public void test_result(){
		//结果
		List<ApiResult> results = doc.getResults();
		assertEquals(3, results.size());
		
		int i = 0;
		ApiResult result = null;
		
		result = results.get(i++);
		assertEquals(null, result.getName());
		assertEquals("Object", result.getType());
		
		
		result = results.get(i++);
		assertEquals("data", result.getName());
		assertEquals("Object", result.getType());
		
		result = results.get(i++);
		assertEquals("data.name", result.getName());
		assertEquals("String", result.getType());
		
	}
}

@RestController
@RequestMapping("/test")
class SimpleGerricRest {
	
	@RequestMapping(value="/one", method=RequestMethod.POST)
	@ApiMethod(name="增加用户", desc="运营人员后台增加用户", requires={"name", "contact.email"})
	public One<Data> add(){
		return null;
	}
}

class One<T>{
	@ApiField(topGenericClass=true)
	T data;
}
class Data{
	String name;
}