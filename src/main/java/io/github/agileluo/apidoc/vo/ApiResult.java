package io.github.agileluo.apidoc.vo;

import java.util.List;

import io.github.agileluo.apidoc.annotation.ApiField;

/**
 * 接口返回参数
 * 
 * @author luoml
 *
 */
public class ApiResult {
	@ApiField(name = "类型", remark = "[String,Boolean,Int,Float,Array,Enum,Date,Url,Email,Object]")
	private String type;
	@ApiField(name = "字段")
	private String name;
	@ApiField(name = "名称")
	private String label;
	@ApiField(name = "枚举列表")
	private List<String> enumerable;
	@ApiField(name = "备注")
	private String remark;
	@ApiField(name = "参数类型")
	private String paramType;
	@ApiField(name = "引用类型", remark = "对象有重复时使用")
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<String> getEnumerable() {
		return enumerable;
	}

	public void setEnumerable(List<String> enumerable) {
		this.enumerable = enumerable;
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
