package io.github.agileluo.apidoc;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;

import io.github.agileluo.apidoc.parser.ParserCommon;

public class ApiCommonParserTest {

	ParserCommon p = new ParserCommon();

	@Test
	public void test_getGenericType() throws Exception {
		Class<SimpleVo> clazz = SimpleVo.class;
		Field name = clazz.getDeclaredField("name");
		Field arr = clazz.getDeclaredField("arr");
		Field list = clazz.getDeclaredField("list");
		Field child = clazz.getDeclaredField("child");
		
		assertEquals(null, p.getGenericType(name));
		assertEquals(String.class, p.getGenericType(arr));
		assertEquals(String.class, p.getGenericType(list));
		assertEquals(SimpleVo.class, p.getGenericType(child));
	}
	static class SimpleVo {
		String name;
		String[] arr;
		List<String> list;
		SimpleVo[] child;
	}
	
	
}
