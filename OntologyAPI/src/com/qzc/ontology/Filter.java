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
		String jsonString = findJsonResult(sparql);
		System.out.println("getApplicationDomain:" + jsonString);
		return jsonString;
	}

	@POST
	@Path("/getApplicationArea/applicationDomain={applicationDomain}")
	@Produces("application/json")
	public String getApplicationArea(@PathParam("applicationDomain") String applicationDomain) {
		// sparql
		String sparql = String.format("SELECT ?applicationArea WHERE {?applicationArea rdfs:subClassOf onto:%s.}",
				applicationDomain);
		// test
		String jsonString = findJsonResult(sparql);
		System.out.println("getApplicationArea:" + jsonString);
		return jsonString;
	}

	@POST
	@Path("/getDataSourceType")
	@Produces("application/json")
	public String getDataSensorType() {
		// sparql
		String sparql = "SELECT ?dataSourceType WHERE {?dataSourceType rdfs:subClassOf onto:DataSourceType.}";
		// test
		String jsonString = findJsonResult(sparql);
		System.out.println("getDataSourceType:" + jsonString);
		return jsonString;
	}

	@POST
	@Path("/getDataFeature")
	@Produces("application/json")
	public String getDataFeature() {
		// sparql
		String sparql = "SELECT DISTINCT ?dataFeature\r\n" + "WHERE {\r\n"
				+ "	?application rdf:type onto:DeepLearningApplication.\r\n"
				+ "	?application onto:applicationName ?applicationName.\r\n"
				+ "	?application onto:hasData ?data.\r\n" + "	?data onto:dataFeature ?dataFeature.}";
		// test
		String jsonString = findJsonResult(sparql);
		System.out.println("getDataFeature:" + jsonString);
		return jsonString;
	}

	@POST
	@Path("/getModelBackend")
	@Produces("application/json")
	public String getBackend() {
		// sparql
		String sparql = "SELECT DISTINCT ?backendName\r\n" + "WHERE {\r\n"
				+ "	?application rdf:type onto:DeepLearningApplication.\r\n"
				+ "	?application onto:applicationName ?applicationName.\r\n"
				+ "	?application onto:hasModel ?model.\r\n" + "	?model onto:hasBackend ?backend.\r\n"
				+ "	?backend onto:backendName ?backendName.\r\n" + "}";
		// test
		String jsonString = findJsonResult(sparql);
		System.out.println("getBackend:" + jsonString);
		return jsonString;
	}

	@POST
	@Path("/getModelType")
	@Produces("application/json")
	public String getModelType() {
		// sparql
		String sparql = "SELECT ?modelType WHERE {?modelType rdfs:subClassOf onto:ModelType.}";
		// test
		String jsonString = findJsonResult(sparql);
		System.out.println("getModelType:" + jsonString);
		return jsonString;
	}

	@POST
	@Path("/getModelLossFunction")
	@Produces("application/json")
	public String getModelLossFunction() {
		// sparql
		String sparql = "SELECT DISTINCT ?lossFunctionName\r\n" + 
				"WHERE {\r\n" + 
				"	?application rdf:type onto:DeepLearningApplication.\r\n" + 
				"	?application onto:applicationName ?applicationName.\r\n" + 
				"	?application onto:hasModel ?model.\r\n" + 
				"	?model onto:hasLossFunction ?lossFunction.\r\n" + 
				"	?lossFunction onto:lossFunctionName ?lossFunctionName.\r\n" + 
				"}";
		// test
		String jsonString = findJsonResult(sparql);
		System.out.println("getModelLossFunction:" + jsonString);
		return jsonString;
	}

	@POST
	@Path("/getModelOptimiser")
	@Produces("application/json")
	public String getModelOptimiser() {
		// sparql
		String sparql = "SELECT DISTINCT ?optimiserName\r\n" + 
				"WHERE {\r\n" + 
				"	?application rdf:type onto:DeepLearningApplication.\r\n" + 
				"	?application onto:applicationName ?applicationName.\r\n" + 
				"	?application onto:hasModel ?model.\r\n" + 
				"	?model onto:hasOptimiser ?optimiser.\r\n" + 
				"	?optimiser onto:optimiserName ?optimiserName.\r\n" + 
				"}";
		// test
		String jsonString = findJsonResult(sparql);
		System.out.println("getModelOptimiser:" + jsonString);
		return jsonString;
	}

	@POST
	@Path("/getCoreLayerType")
	@Produces("application/json")
	public String getCoreLayerType() {
		// sparql
		String sparql = "SELECT ?coreLayerType WHERE {?coreLayerType rdfs:subClassOf onto:CoreLayer. FILTER (?coreLayerType != onto:Bias && ?coreLayerType !=onto:Weight && ?coreLayerType !=onto:ActivationFunction)}";
		// test
		String jsonString = findJsonResult(sparql);
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
		String jsonString = findJsonResult(sparql);
		System.out.println("getFunctionalLayerType:" + jsonString);
		return jsonString;
	}

}
