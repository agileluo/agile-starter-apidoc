package io.github.agileluo.apidoc.vo;

import java.lang.reflect.Parameter;

public class RestParam {
	private Parameter type;
	private String name;
	private String label;
	private String remark;
	private Integer length;
	public Parameter getType() {
		return type;
	}
	public void setType(Parameter type) {
		this.type = type;
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
	public RestParam(Parameter type, String name) {
		super();
		this.type = type;
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public RestParam(Parameter type, String name, String label, String remark, Integer length) {
		super();
		this.type = type;
		this.name = name;
		this.label = label;
		this.remark = remark;
		this.length = length;
	}
	
	
}
