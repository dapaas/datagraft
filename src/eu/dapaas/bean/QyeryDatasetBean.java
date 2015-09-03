package eu.dapaas.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import eu.dapaas.dao.SPARQLResult;
import eu.dapaas.handler.QueryHandler;
import eu.dapaas.utils.Config;
import eu.dapaas.utils.Utils;

public class QyeryDatasetBean {
  public SPARQLResult executeQuery(String apiKey, String apiSecret, String query, String datasetId) throws Exception {
    QueryHandler queryhandler = new QueryHandler(query);
    String responseStr = queryhandler.executeQueryById(apiKey, apiSecret, datasetId);
    JSONObject qresult = Utils.convertStringToJSON(responseStr);
    SPARQLResult result = new SPARQLResult(qresult);
    result.setColumns(Utils.convertColumn(responseStr));
    return result;
  }

  public void exportRDF(HttpServletResponse response, String apiKey, String apiSecret, String username, String datasetId, String contentType) throws JSONException, IOException {
    if (Utils.isEmpty(apiKey)){
      apiKey = null;
    }
    if (Utils.isEmpty(apiSecret)){
      apiSecret = null;
    }
    QueryHandler queryhandler = new QueryHandler();
    File file = queryhandler.exportRDF(apiKey, apiSecret, username, datasetId, contentType);
    if (file != null) {
      response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
      FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
      int i;
      while ((i = fileInputStream.read()) != -1) {
        response.getOutputStream().write(i);
      }
      fileInputStream.close();
      response.getOutputStream().close();
      file.delete();
    }

  }
  
  public void exportRaw(HttpServletResponse response, String apiKey, String apiSecret, String username, String datasetId) throws JSONException, IOException {
    if (Utils.isEmpty(apiKey)){
      apiKey = null;
    }
    if (Utils.isEmpty(apiSecret)){
      apiSecret = null;
    }
    QueryHandler queryhandler = new QueryHandler();
    File file = queryhandler.exportRaw(apiKey, apiSecret, username, datasetId);
    if (file != null) {
      response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
      FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
      int i;
      while ((i = fileInputStream.read()) != -1) {
        response.getOutputStream().write(i);
      }
      fileInputStream.close();
      response.getOutputStream().close();
      file.delete();
    }

  }
	
	public List<String> getDatasetProperties(String apiKey, String apiSecret, String datasetId) throws Exception {
	  try{
	  if (Utils.isEmpty(apiKey)){
	    apiKey = null;
	  }
	  if (Utils.isEmpty(apiSecret)){
	    apiSecret = null;
    }
	    QueryHandler queryhandler = new QueryHandler(Config.getInstance().getDatasetPropertiesQuery());
	    String responseStr = queryhandler.executeQueryById(apiKey, apiSecret, datasetId);
	    JSONObject qresult = Utils.convertStringToJSON(responseStr);
	    SPARQLResult result = new SPARQLResult(qresult);
	    result.setColumns(Utils.convertColumn(responseStr));
	    // return result;
	    
	    List<HashMap<String, String>> propertyValues = result.getValues();
	    List<String> valResult = substringVals(propertyValues);
	    return valResult;
	  }catch(Exception e){
	    e.printStackTrace();
	    return new ArrayList<String>();
	  }
	 }
	
	private List<String> substringVals(List<HashMap<String, String>> propertyValues) {
		
		List<String> vals = new ArrayList<String>();
	    for (HashMap<String, String> map :  propertyValues) {
	    	 for( String val : map.values()) {
	    		Pattern pattern = Pattern.compile("^http://rdf.dapaas.eu/cvsimport/(.*)$");
	 		    Matcher matcher = pattern.matcher(val);
	 		    if(matcher.matches()) {
	 	           vals.add(matcher.group(1));
	 	        }
	    	 }
	    }
		return vals;
	}
}
