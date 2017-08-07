package io.github.agileluo.apidoc.vo;

import java.util.List;

public class ApiFieldType {
	private String type;
	//泛型参数
	private Class<?> clazz;
	private Class<?> genericType;
	
	private String name;
	private String label;
	private String remark;
	private List<String> enumerable;
	private boolean topGenericClass;
	
	public ApiFieldType() {
		super();
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Class<?> getGenericType() {
		return genericType;
	}
	public void setGenericType(Class<?> genericType) {
		this.genericType = genericType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<String> getEnumerable() {
		return enumerable;
	}
	public void setEnumerable(List<String> enumerable) {
		this.enumerable = enumerable;
	}
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	public boolean isTopGenericClass() {
		return topGenericClass;
	}
	public void setTopGenericClass(boolean topGenericClass) {
		this.topGenericClass = topGenericClass;
	}
	
}
