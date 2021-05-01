package com.qzc.ontology;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Entity;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class JerseyClinentGet {
	public static void main(String[] args) {
		Client client = Client.create();
//		WebResource webresource = client.resource(String.format("http://118.139.82.171:8080/OntologyAPI/api/filter/getApplicationDomain"));
//		WebResource webresource = client.resource(String.format("http://118.139.82.171:8080/OntologyAPI/api/filter/getModelType", "Healthcare"));
		ParameterBean variable = new ParameterBean();
		List<String> applicationDomainList = new ArrayList<String>();
		applicationDomainList.add("Healthcare");
		variable.setApplicationDomain(applicationDomainList);
		List<String> applicationAreaList = new ArrayList<String>();
		applicationAreaList.add("MusculoskeletalDisorder");
		applicationAreaList.add("SkinCancer");
		variable.setApplicationArea(applicationAreaList);
		WebResource webresource = client.resource("http://localhost:8080/OntologyAPI/api/filter/getOverviewInformation/jsonParameterBean="+"{\"applicationArea\":[\"SkinCancer\",\"MusculoskeletalDisorder\"],\"applicationDomain\":[\"Healthcare\"]}");
		String response = webresource.type("application/json").post(String.class);
		System.out.print(response);
	}

}
