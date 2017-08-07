package io.github.agileluo.apidoc.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.github.agileluo.apidoc.annotation.ApiField;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiParam {
	@ApiField(name="类型", remark="[String,Boolean,Int,Float,Array,Enum,Date,Url,Email,Object]")
	private String type;
	@ApiField(name="字段")
	private String name;
	@ApiField(name="名称")
	private String label;
	@ApiField(name="是否必填")
	private boolean require;
	
	private Integer length;
	private Integer min;
	private Integer max;
	@ApiField(name="枚举列表")
	private String[] enumerable;
	@ApiField(name="备注")
	private String remark;
	@ApiField(name="参数类型")
	private String paramType;
	@ApiField(name="引用类型", remark="对象有重复时使用")
	private String clazz;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isRequire() {
		return require;
	}
	public void setRequire(boolean require) {
		this.require = require;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getMin() {
		return min;
	}
	public void setMin(Integer min) {
		this.min = min;
	}
	public Integer getMax() {
		return max;
	}
	public void setMax(Integer max) {
		this.max = max;
	}
	public String[] getEnumerable() {
		return enumerable;
	}
	public void setEnumerable(String[] enumerable) {
		this.enumerable = enumerable;
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
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
}
