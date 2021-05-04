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

@Path("search")
public class Search {
	
	Dataset dataset;

	StringBuffer prefix;

	public Search() {
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
	@Path("/getOverviewInformation")
	@Produces("application/json")
	public String getOverviewInformation(String jsonParameterBean) {
		// string to object
		Gson gson = new Gson();
		ParameterBean parameterBean = gson.fromJson(jsonParameterBean, ParameterBean.class);
		// sparql
		String sparql = "SELECT ?applicationName ?data ?dataName ?dataResource ?model ?modelName ?modelResource (COUNT(?modelLayer) as ?numberOfLayers)\r\n" + 
				"?performanceAccuracy ?performancePrecision ?performanceRecall ?performanceF1Score\r\n" + 
				"WHERE {\r\n" + 
				"	?application rdf:type onto:DeepLearningApplication.\r\n" + 
				" 	?application onto:applicationName ?applicationName.\r\n" + 
				"	?application onto:hasData ?data.\r\n" + 
				"	?data onto:dataName ?dataName.\r\n" + 
				"  	?data onto:dataResource ?dataResource.\r\n" + 
				"	?application onto:hasModel ?model.\r\n" + 
				"	?model onto:modelName ?modelName.\r\n" + 
				"  	?model onto:modelResource ?modelResource.\r\n" + 
				"	?model onto:hasPerformance ?modelPerformance.\r\n" + 
				"	?modelPerformance onto:performanceAccuracy ?performanceAccuracy.\r\n" + 
				"	?modelPerformance onto:performancePrecision ?performancePrecision.\r\n" + 
				"	?modelPerformance onto:performanceRecall ?performanceRecall.\r\n" + 
				"	?modelPerformance onto:performanceF1Score ?performanceF1Score.\r\n" + 
				"  	?model onto:hasLayer ?modelLayer.";
		// has parameterBean
		if (parameterBean!=null) {
			
			// has application domain and area
			List<String> applicationDomains = parameterBean.getApplicationDomain();
			if(applicationDomains!=null) {
				sparql+= "  FILTER EXISTS {\r\n";
				for (int i = 0; i<applicationDomains.size(); i++) {
					if(i==0)
					{
						sparql+= "    {?application onto:has"+ applicationDomains.get(i)+" ?applicationDomain.}\r\n";
					}else {
						sparql+= "    UNION \r\n";
						sparql+= "    {?application onto:has"+ applicationDomains.get(i)+" ?applicationDomain.}\r\n";
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
			
			// has data source type
			List<String> dataSourceTypes = parameterBean.getDataSourceType();
			if(dataSourceTypes!=null) {
				sparql+= "  FILTER EXISTS {\r\n";
				sparql+= "		{?data onto:hasDataSourceType ?DataSourceType.}\r\n";
				for (int i = 0; i<dataSourceTypes.size(); i++) {
					if(i==0)
					{
						sparql+= "    {?DataSourceType onto:has"+ dataSourceTypes.get(i)+" ?sensorType.}\r\n";
					}else {
						sparql+= "    UNION \r\n";
						sparql+= "    {?DataSourceType onto:has"+ dataSourceTypes.get(i)+" ?sensorType.}\r\n";
					}
				}
				sparql+= "  }\r\n";
			}
			
			// has model type
			List<String> modelTypes = parameterBean.getModelType();
			if(modelTypes!=null) {
				sparql+= "  FILTER EXISTS {\r\n";
				sparql+= "		{?model onto:hasModelType ?modelType.}\r\n";
				for (int i = 0; i<modelTypes.size(); i++) {
					if(i==0)
					{
						sparql+= "    {?modelType onto:has"+ modelTypes.get(i)+" ?HARModelType.}\r\n";
					}else {
						sparql+= "    UNION \r\n";
						sparql+= "    {?modelType onto:has"+ modelTypes.get(i)+" ?HARModelType.}\r\n";
					}
				}
				sparql+= "  }\r\n";
			}
			
			//has performance metrics
			String accuracy= parameterBean.getAccuracy();
			if(accuracy!=null) {
				sparql+= "  FILTER (?performanceAccuracy >"+ accuracy +")\r\n";
			}
			String precision= parameterBean.getPrecision();
			if(precision!=null) {
				sparql+= "  FILTER (?performancePrecision >"+ precision +")\r\n";
			}
			String recall= parameterBean.getRecall();
			if(recall!=null) {
				sparql+= "  FILTER (?performanceRecall >"+ recall +")\r\n";
			}
			String f1score= parameterBean.getF1score();
			if(f1score!=null) {
				sparql+= "  FILTER (?performanceF1Score >"+ f1score +")\r\n";
			}
			
		}
		sparql+= "}";
		sparql+="GROUP BY \r\n" + 
				"?applicationName ?data ?dataName ?dataResource ?model ?modelName ?modelResource\r\n" + 
				"?performanceAccuracy ?performancePrecision ?performanceRecall ?performanceF1Score";
		
		// test
		String jsonString =  findJsonResult(sparql);
		System.out.println("getOverview:" + jsonString);
		return jsonString;
	}

	public static void main(String args[]) {
		new Search().getOverviewInformation("{\"applicationDomain\":[\"Healthcare\"], \"applicationArea\":[\"MusculoskeletalDisorder\"], \"dataSourceType\":[\"Accelerometer\"], \"modelType\":[\"RNN\"], \"accuracy\":0.9}");
	}
}


