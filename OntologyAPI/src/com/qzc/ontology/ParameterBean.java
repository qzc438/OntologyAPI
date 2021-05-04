package com.qzc.ontology;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParameterBean {
    private List<String> applicationDomain;
    private List<String> applicationArea;
    private List<String> dataSourceType;
    private List<String> modelType;
    private String accuracy;
    private String precision;
    private String recall;
    private String f1score;
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
	public String getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	public String getRecall() {
		return recall;
	}
	public void setRecall(String recall) {
		this.recall = recall;
	}
	public String getF1score() {
		return f1score;
	}
	public void setF1score(String f1score) {
		this.f1score = f1score;
	}
	public ParameterBean(List<String> applicationDomain, List<String> applicationArea, List<String> dataSourceType,
			List<String> modelType, String accuracy, String precision, String recall, String f1score) {
		super();
		this.applicationDomain = applicationDomain;
		this.applicationArea = applicationArea;
		this.dataSourceType = dataSourceType;
		this.modelType = modelType;
		this.accuracy = accuracy;
		this.precision = precision;
		this.recall = recall;
		this.f1score = f1score;
	}
	public ParameterBean() {
		super();
		// TODO Auto-generated constructor stub
	}
}
