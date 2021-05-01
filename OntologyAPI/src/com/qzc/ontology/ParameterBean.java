package com.qzc.ontology;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParameterBean {
    private List<String> applicationDomain;
    private List<String> applicationArea;
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
	public ParameterBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ParameterBean(List<String> applicationDomain, List<String> applicationArea) {
		super();
		this.applicationDomain = applicationDomain;
		this.applicationArea = applicationArea;
	}
}
