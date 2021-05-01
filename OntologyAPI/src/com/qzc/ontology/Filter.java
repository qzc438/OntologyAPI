package com.qzc.ontology;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

import com.google.gson.Gson;
import com.qzc.test.ResponseMessage;
import com.qzc.util.Naming;
import com.qzc.util.TDBDatabase;
import com.sun.jersey.api.Responses;

@Path("filter")
public class Filter {
	
	Dataset dataset;

	StringBuffer prefix;

	public Filter() {
		// init dataset
		dataset = TDBDatabase.getTDBDatabase().getDataset();
		// init prefix
		prefix = new StringBuffer();
		prefix.append("prefix onto: <" + Naming.DEFAULT_NAME_SPACE + ">\n");
		prefix.append("prefix xsd: <" + XSD.getURI() + ">\n");
		prefix.append("prefix owl: <" + OWL.getURI() + ">\n");
		prefix.append("prefix rdf: <" + RDF.getURI() + ">\n");
		prefix.append("prefix rdfs: <" + RDFS.getURI() + ">\n");
	}
	
	// find json result
	public String findJsonResult(String sparql) {
		// query
		String SPARQL = prefix.toString() + sparql.toString();
		// query model
		dataset.begin(ReadWrite.READ);
		Model model = dataset.getNamedModel(Naming.TDB_MODEL);
		Query query = QueryFactory.create(SPARQL);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet rs = qe.execSelect();
		// write to a ByteArrayOutputStream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(outputStream, rs);
		// turn that into a String
		String str = new String(outputStream.toByteArray());
		qe.close();
		dataset.commit();
		dataset.end();
		return str;
	}

	
	@POST
	@Path("/getApplicationDomain")
	@Produces("application/json")
	public String getApplicationDomain() {
		// sparql
		String sparql = "SELECT ?applicationDomain WHERE {?applicationDomain rdfs:subClassOf onto:DeepLearningApplication.}";
		// test
		String jsonString =  findJsonResult(sparql);
		System.out.println("getApplicationDomain:" + jsonString);
		return jsonString;
	}
	
	@POST
	@Path("/getApplicationArea/applicationDomain={applicationDomain}")
	@Produces("application/json")
	public String getApplicationArea(@PathParam("applicationDomain") String applicationDomain) {
		// sparql
		String sparql = String.format("SELECT ?applicationArea WHERE {?applicationArea rdfs:subClassOf onto:%s.}", applicationDomain);
		// test
		String jsonString =  findJsonResult(sparql);
		System.out.println("getApplicationArea:" + jsonString);
		return jsonString;
	}
	
	@POST
	@Path("/getDataSensorType")
	@Produces("application/json")
	public String getDataSensorType() {
		// sparql
		String sparql = "SELECT ?dataSourceType WHERE {?dataSourceType rdfs:subClassOf onto:DataSourceType.}";
		// test
		String jsonString =  findJsonResult(sparql);
		System.out.println("getDataSensorType:" + jsonString);
		return jsonString;
	}
	
	@POST
	@Path("/getModelType")
	@Produces("application/json")
	public String getModelType() {
		// sparql
		String sparql = "SELECT ?modelType WHERE {?modelType rdfs:subClassOf onto:ModelType.}";
		// test
		String jsonString =  findJsonResult(sparql);
		System.out.println("getModelType:" + jsonString);
		return jsonString;
	}
	
	@POST
	@Path("/getLayerType")
	@Produces("application/json")
	public String getLayerType() {
		// sparql
		String sparql = "SELECT ?layerType WHERE {?layerType rdfs:subClassOf onto:ModelLayer.}";
		// test
		String jsonString =  findJsonResult(sparql);
		System.out.println("getLayerType:" + jsonString);
		return jsonString;
	}
	
	@POST
	@Path("/getCoreLayerType")
	@Produces("application/json")
	public String getCoreLayerType() {
		// sparql
		String sparql = "SELECT ?coreLayerType WHERE {?coreLayerType rdfs:subClassOf onto:CoreLayerType.}";
		// test
		String jsonString =  findJsonResult(sparql);
		System.out.println("getCoreLayerType:" + jsonString);
		return jsonString;
	}
	
	@POST
	@Path("/getFunctionalLayerType")
	@Produces("application/json")
	public String getFunctionalLayerType() {
		// sparql
		String sparql = "SELECT ?functionalLayerType WHERE {?functionalLayerType rdfs:subClassOf onto:FunctionalLayer.}";
		// test
		String jsonString =  findJsonResult(sparql);
		System.out.println("getFunctionalLayerType:" + jsonString);
		return jsonString;
	}
	
	@POST
	@Path("/getOverviewInformation")
	@Produces("application/json")
	public String getOverviewInformation(String jsonParameterBean) {
		// string to object
		Gson gson = new Gson();
		ParameterBean parameterBean = gson.fromJson(jsonParameterBean, ParameterBean.class);
		// sparql
		String sparql = "SELECT ?applicationName ?data ?dataName ?model ?modelName \r\n" + 
				"?performanceAccuracy ?performancePrecision ?performanceRecall ?performanceF1Score\r\n" + 
				"WHERE {\r\n" + 
				"	?application rdf:type onto:DeepLearningApplication.\r\n" + 
				" 	?application onto:applicationName ?applicationName.\r\n" + 
				"	?application onto:hasData ?data.\r\n" + 
				"	?data onto:dataName ?dataName.\r\n" + 
				"	?application onto:hasModel ?model.\r\n" + 
				"	?model onto:modelName ?modelName.\r\n" + 
				"	?model onto:hasPerformance ?modelPerformance.\r\n" + 
				"	?modelPerformance onto:performanceAccuracy ?performanceAccuracy.\r\n" + 
				"	?modelPerformance onto:performancePrecision ?performancePrecision.\r\n" + 
				"	?modelPerformance onto:performanceRecall ?performanceRecall.\r\n" + 
				"	?modelPerformance onto:performanceF1Score ?performanceF1Score.";
		// has parameterBean
		if (parameterBean!=null) {
			sparql+= "  FILTER EXISTS {\r\n";
			// has application domain
			List<String> applicationDomains = parameterBean.getApplicationDomain();
			if(applicationDomains!=null) {
				for (int i = 0; i<applicationDomains.size(); i++) {
					if(i==0)
					{
						sparql+= "    {?application onto:has"+ applicationDomains.get(i)+" ?applicationDomain.}\r\n";
					}else {
						sparql+= "    UNION \r\n";
						sparql+= "    {?application onto:has"+ applicationDomains.get(i)+" ?applicationDomain.}\r\n";
					}
				}
			}
			// has application area
			List<String> applicationAreas = parameterBean.getApplicationArea();
			if(applicationAreas!=null) {
				for (int i = 0; i<applicationAreas.size(); i++) {
					if(i==0)
					{
						sparql+= "    {?applicationDomain onto:has"+ applicationAreas.get(i)+" ?applicationArea.}\r\n";
					}else {
						sparql+= "    UNION \r\n";
						sparql+= "    {?applicationDomain onto:has"+ applicationAreas.get(i)+" ?applicationArea.}\r\n";
					}
				}
			}
			sparql+= "  }\r\n";
		}
		sparql+= "}";
		
		// test
		String jsonString =  findJsonResult(sparql);
		System.out.println("getOverview:" + jsonString);
		return jsonString;
	}

	public static void main(String args[]) {
		new Filter().getOverviewInformation("{\"applicationArea\":[\"SkinCancer\"],\"applicationDomain\":[\"Healthcare\"]}");
	}
}


