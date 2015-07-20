package eu.dapaas.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sirmamobile.commlib.annotations.MappedObject;
import com.sirmamobile.commlib.annotations.Oneway;

/*{
 "head": {
 "vars": [ "loc", "person" ]
 }, 
 "results": {
 "bindings": [
 {
 "loc": { "type": "uri", "value": "http:\/\/dbpedia.org\/resource\/Manhattan" }, 
 "person": { "type": "uri", "value": "http:\/\/dbpedia.org\/resource\/Anthony_Perkins" }
 }, 
 ]
 }
 }*/
@Oneway
@MappedObject(recursive = true, forced = true)
public class SPARQLResult {
  // private static final Logger logger = Logger.getLogger(SPARQLResult.class);

  private List<String>                  keys    = new ArrayList<String>();
  private List<HashMap<String, String>> values  = new ArrayList<HashMap<String, String>>();
  private HashMap<String, String>       columns = new HashMap<String, String>();
  private String                        error;
  private JSONObject                  poligon;

  public SPARQLResult() {

  }

  public SPARQLResult(JSONObject o) {
    try {

      error = o.getString("response");
    } catch (JSONException e) {
      // e.printStackTrace();
    }
    try {
      JSONArray dis = (JSONArray) ((JSONObject) o.get("head")).get("vars");
      if (dis != null) {
        for (int i = 0; i < dis.length(); i++) {
          this.keys.add(dis.get(i).toString());
        }
      }
    } catch (JSONException e) {
      // e.printStackTrace();
    }
    try {
      JSONArray results = (JSONArray) ((JSONObject) o.get("results")).get("bindings");
      if (results != null) {
        for (int i = 0; i < results.length(); i++) {
          JSONObject itemJson = (JSONObject) results.get(i);
          HashMap<String, String> item = new HashMap<String, String>();
          for (String key : keys) {
            JSONObject row = (JSONObject) itemJson.get(key);
            String value = row.getString("value");
            item.put(key, value);
          }
          this.values.add(item);
        }
      }

    } catch (JSONException e) {
      // logger.error("", e);
    }
  }

  public List<String> getKeys() {
    return keys;
  }

  public void setKeys(List<String> keys) {
    this.keys = keys;
  }

  public List<HashMap<String, String>> getValues() {
    return values;
  }

  public void setValues(List<HashMap<String, String>> values) {
    this.values = values;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public HashMap<String, String> getColumns() {
    return columns;
  }

  public void setColumns(HashMap<String, String> columns) {
    this.columns = columns;
  }

  public JSONObject getPoligon() {
    return poligon;
  }

  public void setPoligon(JSONObject poligon) {
    this.poligon = poligon;
  }

}
