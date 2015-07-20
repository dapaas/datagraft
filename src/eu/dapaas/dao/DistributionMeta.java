package eu.dapaas.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DistributionMeta {
  private static final Logger logger  = Logger.getLogger(DistributionMeta.class);

  /*
   * "@context":{"dcat":"http://www.w3.org/ns/dcat#",
   * "dct":"http://purl.org/dc/terms/"
   * ,"dct:issued":{"@type":"xsd:date"},"dct:modified":{"@type":"xsd:date"}},
   * "@type":"dcat:Distribution", "dct:issued":"2014-08-18",
   * "dct:modified":"2014-08-17",
   * "dct:title":"Title for Distribution for raw file",
   * "dct:description":"Description for Distribution raw file",
   * "dcat:mediaType":"text/csv", "dcat:fileName":"White_House_staff.csv"
   * "dcat:keyword":["Dataset","drugo","DaPaaS"]
   */

  private String              issued;
  private String              modified;
  private String              title;
  private String              description;
  private String              mediaType;
  private String              fileName;
  private List<String>        keyword = new ArrayList<String>();

  public DistributionMeta(JSONObject o) {
    try {
      this.issued = o.getString("dct:issued");
    } catch (JSONException e) {
      // logger.error("", e);
    }

    try {
      this.modified = o.getString("dct:modified");
    } catch (JSONException e) {
      // logger.error("", e);
    }
    try {
      this.title = o.getString("dct:title");
    } catch (JSONException e) {
      // logger.error("", e);
    }

    try {
      this.description = o.getString("dct:description");
    } catch (JSONException e) {
      // logger.error("", e);
    }
    try {
      this.mediaType = o.getString("dcat:mediaType");
    } catch (JSONException e) {
      // logger.error("", e);
    }
    try {
      this.fileName = o.getString("dcat:fileName");
    } catch (JSONException e) {
      // logger.error("", e);
    }

    try {
      JSONArray kws = (JSONArray) o.get("dcat:keyword");
      if (kws != null) {
        for (int i = 0; i < kws.length(); i++) {
          this.keyword.add(kws.get(i).toString());
        }
      }
    } catch (JSONException e) {
      // e.printStackTrace();
    }

  }

  /*
   * "@context":{"dcat":"http://www.w3.org/ns/dcat#",
   * "dct":"http://purl.org/dc/terms/" ,"dct:issued":{"@type":"xsd:date"},
   * "dct:modified":{"@type":"xsd:date"}}, "@type":"dcat:Distribution",
   * "dct:issued":"2014-08-18", "dct:modified":"2014-08-17",
   * "dct:title":"Title for Distribution for raw file",
   * "dct:description":"Description for Distribution raw file",
   * "dcat:mediaType":"text/csv", "dcat:fileName":"White_House_staff.csv"
   * "dcat:keyword":["Dataset","drugo","DaPaaS"]
   */
  public JSONObject toJSON() {
    try {
      JSONObject jsondate = new JSONObject();
      jsondate.put("@type", "xsd:date");

      JSONObject jsoncontext = new JSONObject();
      jsoncontext.put("dcat", "http://www.w3.org/ns/dcat#");
      // jsoncontext.put("foaf", "http://xmlns.com/foaf/0.1/");
      jsoncontext.put("dct", "http://purl.org/dc/terms/");
      // jsoncontext.put("xsd", "http://www.w3.org/2001/XMLSchema#");
      jsoncontext.put("dct:issued", jsondate);
      jsoncontext.put("dct:modified", jsondate);
      // jsoncontext.put("foaf:primaryTopic", jsonid);
      // jsoncontext.put("dcat:distribution", jsonid);

      JSONObject json = new JSONObject();
      json.put("@context", jsoncontext);
      json.put("@type", "dcat:Distribution");
      json.put("dct:issued", this.issued);
      json.put("dct:modified", this.modified);
      json.put("dct:title", this.title);
      json.put("dct:description", this.description);
      json.put("dcat:mediaType", this.mediaType);
      json.put("dcat:fileName", this.fileName);
//      JSONArray array = new JSONArray();
//      for (String key : keyword) {
//        array.put(key);
//      }
//      json.put("dcat:keyword", array);

      return json;
    } catch (JSONException e) {
      logger.error("", e);
      return new JSONObject();
    }
  }

  public String getIssued() {
    return issued;
  }

  public void setIssued(String issued) {
    this.issued = issued;
  }

  public String getModified() {
    return modified;
  }

  public void setModified(String modified) {
    this.modified = modified;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getMediaType() {
    return mediaType;
  }

  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public List<String> getKeyword() {
    return keyword;
  }

  public void setKeyword(List<String> keyword) {
    this.keyword = keyword;
  }

}
