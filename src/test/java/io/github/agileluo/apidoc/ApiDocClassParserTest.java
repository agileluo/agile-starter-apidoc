package io.github.agileluo.apidoc;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.github.agileluo.apidoc.annotation.ApiField;
import io.github.agileluo.apidoc.annotation.ApiMethod;
import io.github.agileluo.apidoc.parser.ApiDocClassParser;
import io.github.agileluo.apidoc.vo.ApiDoc;
import io.github.agileluo.apidoc.vo.ApiParam;
import io.github.agileluo.apidoc.vo.ApiResult;

public class ApiDocClassParserTest {
	
	private String appName = "testRest";
	private ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
	
	List<ApiDoc> docs;
	private ApiDoc doc;
	
	@Before
	public void before() throws ClassNotFoundException{
		docs = ApiDocClassParser.parse(SimpleRest.class, appName, nameDiscoverer);
		doc = docs.get(0);
	}
	
	@Test
	public void test_api(){
		assertEquals(1, docs.size());
		assertEquals("/testRest/test/add", doc.getUrl());
		assertArrayEquals(new String[]{"POST"}, doc.getMethod());
		assertEquals("增加用户", doc.getName());
		assertEquals("运营人员后台增加用户", doc.getDescription());
	}
	
	@Test
	public void test_param(){
		List<ApiParam> params = doc.getParameters();
		assertEquals(7, params.size());
		
		int i = 0;
		ApiParam param = null;
		
		param = params.get(i++);
		assertEquals("version", param.getName());
		assertEquals("String", param.getType());
		assertEquals("版本号", param.getLabel());
		
		
		param = params.get(i++);
		assertEquals("name", param.getName());
		assertEquals("String", param.getType());
		assertEquals(Integer.valueOf(20), param.getLength());
		assertEquals("名称", param.getLabel());
		assertEquals(true, param.isRequire());
		
		param = params.get(i++);
		assertEquals("favs", param.getName());
		assertEquals("Array", param.getType());
		assertEquals("喜好", param.getLabel());
		assertEquals("String", param.getParamType());
		
		param = params.get(i++);
		assertEquals("children", param.getName());
		assertEquals("Array", param.getType());
		assertEquals("孩子", param.getLabel());
		assertEquals("person", param.getClazz());
		
		param = params.get(i++);
		assertEquals("contact", param.getName());
		assertEquals("Object", param.getType());
		assertEquals("联系方式", param.getLabel());
		
		param = params.get(i++);
		assertEquals("contact.mobile", param.getName());
		assertEquals("String", param.getType());
		assertEquals("手机号码", param.getLabel());
		
		param = params.get(i++);
		assertEquals("contact.email", param.getName());
		assertEquals("String", param.getType());
		assertEquals("电子邮箱", param.getLabel());
		assertEquals(true, param.isRequire());
	}
	@Test
	public void test_result(){
		//结果
		List<ApiResult> results = doc.getResults();
		assertEquals(8, results.size());
		
		int i = 0;
		ApiResult result = null;
		
		result = results.get(i++);
		assertEquals(null, result.getName());
		assertEquals("Object", result.getType());
		assertEquals(null, result.getLabel());
		
		
		result = results.get(i++);
		assertEquals("version", result.getName());
		assertEquals("String", result.getType());
		assertEquals("版本号", result.getLabel());
		
		result = results.get(i++);
		assertEquals("name", result.getName());
		assertEquals("String", result.getType());
		assertEquals("名称", result.getLabel());
		
		result = results.get(i++);
		assertEquals("favs", result.getName());
		assertEquals("Array", result.getType());
		assertEquals("喜好", result.getLabel());
		assertEquals("String", result.getParamType());
		
		result = results.get(i++);
		assertEquals("children", result.getName());
		assertEquals("Array", result.getType());
		assertEquals("孩子", result.getLabel());
		assertEquals("ROOT", result.getClazz());
		
		result = results.get(i++);
		assertEquals("contact", result.getName());
		assertEquals("Object", result.getType());
		assertEquals("联系方式", result.getLabel());
		
		result = results.get(i++);
		assertEquals("contact.mobile", result.getName());
		assertEquals("String", result.getType());
		assertEquals("手机号码", result.getLabel());
		
		result = results.get(i++);
		assertEquals("contact.email", result.getName());
		assertEquals("String", result.getType());
		assertEquals("电子邮箱", result.getLabel());
	}
}

@RestController
@RequestMapping("/test")
class SimpleRest {
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ApiMethod(name="增加用户", desc="运营人员后台增加用户", requires={"name", "contact.email"})
	public Person add(@RequestBody Person person){
		return null;
	}
}
class Base{
	@ApiField(name="版本号")
	private String version;
}
class Person extends Base{
	
	@ApiField(name="名称", length=20)
	private String name;
	
	@ApiField(name="喜好")
	private String[] favs;
	
	@ApiField(name="孩子")
	private List<Person> children;
	
	@ApiField(name="联系方式")
	private Contact contact;
}

class Contact{
	
	@ApiField(name = "手机号码")
	private String mobile;
	
	@ApiField(name="电子邮箱")
	private String email;
}
