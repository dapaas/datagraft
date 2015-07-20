package eu.dapaas.dao;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class DistributionDetail {
  private static final Logger logger = Logger.getLogger(DistributionDetail.class);
  /*
   * {"@context": { "dct:issued":{"@type":"xsd:date"},
   * "dcat":"http://www.w3.org/ns/dcat#", "foaf:primaryTopic":{"@type":"@id"},
   * "foaf":"http://xmlns.com/foaf/0.1/", "dcat:distribution":{"@type":"@id"},
   * "dct":"http://purl.org/dc/terms/",
   * "xsd":"http://www.w3.org/2001/XMLSchema#",
   * "dct:modified":{"@type":"xsd:date"}}, "@type":"dcat:Distribution",
   * "dcat:accessURL":{"@id":
   * "http://192.168.130.225:8080/openrdf-sesame/repositories/yordanka-test4_null-5"
   * },
   * "@id":"http://eu.dapaas/users/yordanka-test4/distribution/newDistribution-5"
   * }
   * 
   * {"@context":{ 
   * 
   * "dcat":"http://www.w3.org/ns/dcat#", 
   * "foaf:primaryTopic":{"@type":"@id"},
   * "foaf":"http://xmlns.com/foaf/0.1/", 
   * "dcat:distribution":{"@type":"@id"},
   * "dct":"http://purl.org/dc/terms/",
   * "xsd":"http://www.w3.org/2001/XMLSchema#",
   * "dct:issued":{"@type":"xsd:date"},
   * "dct:modified":{"@type":"xsd:date"}
   * },
   * 
   * "dct:title":"undefined title",
   * "dct:fileID":"8b932bfd-07b8-476b-b4f2-fd099bb6ddd1.bin",
   * "dct:description":"undefined description", "@type":"dcat:Distribution",
   * "@id":"http://dapaas.eu/users/1505271111/distribution/undefined+title-5"}
   */

  private String              id;
  private String              description;
  private String              fileId;
  private String              title;
  // FIXME: this parameter is fake, @Deprecated
  private String accessURL;

  // private String accessURL;

  public DistributionDetail(JSONObject o) {
    try {
      this.id = o.getString("@id");
    } catch (JSONException e) {
      logger.error("", e);
    }

    try {
      this.title = o.getString("dct:title");
    } catch (JSONException e) {
      logger.error("", e);
    }
    try {
      this.fileId = o.getString("dct:fileID");
    } catch (JSONException e) {
      logger.error("", e);
    }
    try {
      this.description = o.getString("dct:description");
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
      json.put("@type", "dcat:Distribution");
      json.put("@id", this.id);
      json.put("dct:title", this.title);
      json.put("dct:fileID", this.fileId);
      json.put("dct:description", this.description);
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getFileId() {
    return fileId;
  }

  public void setFileId(String fileId) {
    this.fileId = fileId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAccessURL() {
    return accessURL;
  }

  public void setAccessURL(String accessURL) {
    this.accessURL = accessURL;
  }

}
