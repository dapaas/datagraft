package eu.dapaas.dao;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ReleaseDetail {
  private static final Logger logger       = Logger.getLogger(ReleaseDetail.class);
  /*
   * {"@context":
{"dct:issued":{"@type":"xsd:date"},
"dcat":"http://www.w3.org/ns/dcat#",
"foaf:primaryTopic":{"@type":"@id"},
"foaf":"http://xmlns.com/foaf/0.1/",
"dcat:distribution":{"@type":"@id"},
"dct":"http://purl.org/dc/terms/",
"xsd":"http://www.w3.org/2001/XMLSchema#",
"dct:modified":{"@type":"xsd:date"}},
"@type":"dcat:Release",
"@id":"http://dapaas.eu/users/yordanka-test4/release/newRelease-1"}
  }
  */
    
    private String       id;
    private String accessURL;

    public ReleaseDetail(JSONObject o) {
      try {
        this.id = o.getString("@id");
      } catch (JSONException e) {
        logger.error("", e);
      }
     
      try {
        JSONObject kws = (JSONObject) o.get("dcat:accessURL");
        if (kws != null) {
            this.accessURL = kws.get("@id").toString();
        }
      } catch (JSONException e) {
        logger.error("", e);
      }
      
    }

    public JSONObject toJSON() {
      try {
        JSONObject jsondate = new JSONObject();
        jsondate.put("@type", "xsd:date");
        JSONObject jsonid = new JSONObject();
        jsonid.put("@type", "@id");

        JSONObject jsoncontext = new JSONObject();
        jsoncontext.put("dcat", "http://www.w3.org/ns/dcat#");
        jsoncontext.put("foaf", "http://xmlns.com/foaf/0.1/");
        jsoncontext.put("dct", "http://purl.org/dc/terms/");
        jsoncontext.put("xsd", "http://www.w3.org/2001/XMLSchema#");
        jsoncontext.put("dct:issued", jsondate);
        jsoncontext.put("dct:modified", jsondate);
        jsoncontext.put("foaf:primaryTopic", jsonid);
        jsoncontext.put("dcat:distribution", jsonid);

        JSONObject json = new JSONObject();
        json.put("@context", jsoncontext);
        json.put("@type", "dcat:Release");
        json.put("@id", this.id);
        JSONObject array = new JSONObject();
        array.put("@id", this.accessURL);
        json.put("dcat:accessURL", array);
        return json;
      } catch (JSONException e) {
        logger.error("", e);
        return new JSONObject();
      }
    }
    
    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getAccessURL() {
      return accessURL;
    }

    public void setAccessURL(String accessURL) {
      this.accessURL = accessURL;
    }
}
