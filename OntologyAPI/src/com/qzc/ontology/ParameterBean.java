package com.qzc.ontology;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParameterBean {
    private List<String> applicationDomain;
    private List<String> applicationArea;
    private List<String> dataSourceType;
    private List<String> modelType;
	public List<String> getApplicationDomain() {
		return applicationDomain;
	}
	public void setApplicationDomain(List<String> applicationDomain) {
		this.applicationDomain = applicationDomain;
	}
	public List<String> getApplicationArea() {
		return applicationArea;
	}
	public void setApplicationArea(List<String> applicationArea) {
		this.applicationArea = applicationArea;
	}
	public List<String> getDataSourceType() {
		return dataSourceType;
	}
	public void setDataSourceType(List<String> dataSourceType) {
		this.dataSourceType = dataSourceType;
	}
	public List<String> getModelType() {
		return modelType;
	}
	public void setModelType(List<String> modelType) {
		this.modelType = modelType;
	}
	public ParameterBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ParameterBean(List<String> applicationDomain, List<String> applicationArea, List<String> dataSourceType,
			List<String> modelType) {
		super();
		this.applicationDomain = applicationDomain;
		this.applicationArea = applicationArea;
		this.dataSourceType = dataSourceType;
		this.modelType = modelType;
	}
}
