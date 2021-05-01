package com.qzc.test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class JerseyClinentGet {
	public static void main(String[] args) {
		Client client = Client.create();
		WebResource webresource = client.resource(String.format("http://118.139.82.171:8080/RestDemo/api/login/username=%s&password=%s", "admin", "admin"));
		String response = webresource.type("application/json").post(String.class);
		System.out.print(response);
	}

}
