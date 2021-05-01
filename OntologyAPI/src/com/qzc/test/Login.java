package com.qzc.test;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.sun.jersey.api.Responses;

@Path("login")
public class Login {
	@GET
	@Produces("application/xml")
	public String requestLogin() {
		StringBuilder sb = new StringBuilder();
		sb.append("<note>");
		sb.append("</note>");
		return sb.toString();
	}
	
	@POST
	@Path("/username={p1}&password={p2}")
	@Produces("application/json")
	public String login(@PathParam("p1") String username, @PathParam("p2") String password) {
		ResponseMessage msg = null;
		if(username.equals("admin") && password.equals("admin")) {
			msg = ResponseMessage.success();
		}else {
			msg = ResponseMessage.fail();
		}
		
		return new Gson().toJson(msg);
	}
}


