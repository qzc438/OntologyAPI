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

@Path("detail")
public class Detail {
	
	Dataset dataset;

	StringBuffer prefix;

	public Detail() {
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
	@Path("/getDataDetail/dataID={dataID}")
	@Produces("application/json")
	public String getDataDetail(@PathParam("dataID") String dataID) {
		// sparql
		// String sparql = String.format("SELECT ?applicationArea WHERE {?applicationArea rdfs:subClassOf onto:%s.}", applicationDomain);
		String sparql = String.format("SELECT ?dataName ?dataFeature ?dataDescription ?dataResource ?accelerometerName ?gyroscopeName\r\n" + 
				"WHERE {\r\n" + 
				"	onto:%s onto:dataName ?dataName.\r\n" + 
				"	onto:%s onto:dataFeature ?dataFeature.\r\n" + 
				"  	onto:%s onto:dataDescription ?dataDescription.\r\n" + 
				"  	onto:%s onto:dataResource ?dataResource.\r\n" + 
				"  	onto:%s onto:hasDataSourceType ?DataSourceType.\r\n" + 
				"   OPTIONAL{?DataSourceType onto:hasAccelerometer ?accelerometer. ?accelerometer onto:accelerometerName ?accelerometerName.}\r\n" + 
				"   OPTIONAL{?DataSourceType onto:hasGyroscope ?gyroscope. ?gyroscope onto:gyroscopeName ?gyroscopeName.}\r\n" + 
				"}", dataID, dataID, dataID, dataID, dataID);
		// test
		String jsonString =  findJsonResult(sparql);
		System.out.println("getDataDetail:" + jsonString);
		return jsonString;
	}
	
	
	@POST
	@Path("/getModelDetail/modelID={modelID}")
	@Produces("application/json")
	public String getModelDetail(@PathParam("modelID") String modelID) {
		// sparql
		String sparql = String.format("SELECT ?modelName ?modelDescription ?modelResource \r\n" + 
				"?performanceAccuracy ?performancePrecision ?performanceRecall ?performanceF1Score\r\n" + 
				"?CNNTypeName ?RNNTypeName\r\n" + 
				"WHERE {\r\n" + 
				"	onto:%s onto:modelName ?modelName.\r\n" + 
				"  	onto:%s onto:modelDescription ?modelDescription.\r\n" + 
				"  	onto:%s onto:modelResource ?modelResource.\r\n" + 
				"   onto:%s onto:hasPerformance ?modelPerformance.\r\n" + 
				"	onto:%s onto:hasModelType ?modelType.\r\n" + 
				"   OPTIONAL{?modelPerformance onto:performanceAccuracy ?performanceAccuracy.}\r\n" + 
				"   OPTIONAL{?modelPerformance onto:performancePrecision ?performancePrecision.}\r\n" + 
				"   OPTIONAL{?modelPerformance onto:performanceRecall ?performanceRecall.}\r\n" + 
				"   OPTIONAL{?modelPerformance onto:performanceF1Score ?performanceF1Score.}\r\n" + 
				"   OPTIONAL{?modelType onto:hasCNN ?CNNType. ?CNNType onto:CNNTypeName ?CNNTypeName.}\r\n" + 
				"   OPTIONAL{?modelType onto:hasRNN ?RNNType. ?RNNType onto:RNNTypeName ?RNNTypeName.}\r\n" + 
				"}", modelID, modelID, modelID, modelID, modelID);
		// test
		String jsonString =  findJsonResult(sparql);
		System.out.println("getModelDetail:" + jsonString);
		return jsonString;
	}
	
	
	public static void main(String args[]) {
		new Detail().getModelDetail("model-8583827882eb42dba666e5ed6bbcde92");
	}
	

	
}


