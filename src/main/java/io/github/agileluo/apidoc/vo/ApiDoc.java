package io.github.agileluo.apidoc.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.github.agileluo.apidoc.annotation.ApiField;


/**
 * api文档实例O
 * @author luoml
 *
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiDoc {
	
	@ApiField(name="主键")
	private Long id;
	@ApiField(name="分类Id")
	private Long catalogId;
	@ApiField(name="分类名称")
	private String catalog;

	@ApiField(name="显示顺序")
	private Integer sortNum;
	@ApiField(name="系统id")
	private Integer systemId;
	@ApiField(name="服务id")
	private String serviceId;
	
	@ApiField(name="是否为开放接口")
    private Boolean open;
	
	@ApiField(name="接口地址")
    private String url;
	
	@ApiField(name="请求方法", remark="[GET,POST]")
    private String[] method;
	
	@ApiField(name="参数类型", remark="[json,]")
    private String dataType;
    
	@ApiField(name="名称")
    private String name;
	
	@ApiField(name="描述")
    private String description;
	
	@ApiField(name="入参列表")
    private List<ApiParam> parameters;
	
	@ApiField(name="结果列表")
    private List<ApiResult> results;
    
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String[] getMethod() {
		return method;
	}
	public void setMethod(String[] method) {
		this.method = method;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<ApiParam> getParameters() {
		return parameters;
	}
	public void setParameters(List<ApiParam> parameters) {
		this.parameters = parameters;
	}
	public List<ApiResult> getResults() {
		return results;
	}
	public void setResults(List<ApiResult> results) {
		this.results = results;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public Integer getSortNum() {
		return sortNum;
	}
	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}
}
