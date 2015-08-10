package eu.dapaas.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Dataset {
  private static final Logger logger       = Logger.getLogger(Dataset.class);
  /*
   * {"@context": { "dcat":"http://www.w3.org/ns/dcat#",
   * "foaf":"http://xmlns.com/foaf/0.1/", "dct":"http://purl.org/dc/terms/",
   * "xsd":"http://www.w3.org/2001/XMLSchema#",
   * "dct:issued":{"@type":"xsd:date"}, "dct:modified":{"@type":"xsd:date"},
   * "foaf:primaryTopic":{"@type":"@id"}, "dcat:distribution":{"@type":"@id"} },
   * 
   * ------- for create "@type":"dcat:Dataset",
   * "@id":"http://eu.dapaas/dataset/2", "dct:issued":"2014-09-15",
   * "dct:modified":"2014-09-17", "dct:title":"My second DaPaaS dataset",
   * "dct:description":"Sample DS 2 descr", "dct:publisher":"Alex",
   * "dcat:keyword":[], "dcat:distribution":[] } -------- for catalog
   * "@type":"dcat:CatalogRecord",
   * "foaf:primaryTopic":"http://eu.dapaas/dataset/1",
   * "dct:issued":"2014-09-16", "dct:modified":"2014-09-17",
   * "dct:title":"My first DaPaaS dataset"
   */

  private String              id;
  private String              issued;
  private String              modified;
  private String              title;
  private String              description;
  private boolean             exposePublic;
  private String              publisher;
  private List<String>        keyword      = new ArrayList<String>();
  private List<String>        distribution = new ArrayList<String>();
  private String              portalParameter;
  private String              accessURL;
  private String              fileId;

  // private

  public Dataset(JSONObject o) {
    try {
      this.id = o.getString("@id");
    } catch (JSONException e) {
      // e.printStackTrace();
      try {
        this.id = o.getString("foaf:primaryTopic");
      } catch (JSONException ee) {

      }
    }
    try {
      this.issued = o.getString("dct:issued");
    } catch (JSONException e) {
      // e.printStackTrace();
    }
    try {
      this.modified = o.getString("dct:modified");
    } catch (JSONException e) {
      logger.error("", e);
    }
    try {
      this.title = o.getString("dct:title");
    } catch (JSONException e) {
      // e.printStackTrace();
    }
    try {
      this.description = o.getString("dct:description");
    } catch (JSONException e) {
      // e.printStackTrace();
    }
    try {
      this.publisher = o.getString("dct:publisher");
    } catch (JSONException e) {
      // e.printStackTrace();
    }
    // dct:public
    // dcat:public":"true"
    try {
      this.exposePublic = o.getBoolean("dct:public");
    } catch (JSONException e) {
      // e.printStackTrace();
      try {
        this.exposePublic = o.getBoolean("dcat:public");
      } catch (JSONException ee) {

      }
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
    try {
      JSONArray dis = (JSONArray) o.get("dcat:distribution");
      if (dis != null) {
        for (int i = 0; i < dis.length(); i++) {
          this.distribution.add(dis.get(i).toString());
        }
      }
    } catch (JSONException e) {
      // e.printStackTrace();
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
      json.put("@type", "dcat:Dataset");
      json.put("@id", this.id);
      json.put("dct:issued", this.issued);
      json.put("dct:modified", this.modified);
      json.put("dct:title", this.title);
      json.put("dct:description", this.description);
      json.put("dct:publisher", this.publisher);
      json.put("dcat:public", this.exposePublic);
      JSONArray array = new JSONArray();
      for (String key : keyword) {
        array.put(key);
      }
      json.put("dcat:keyword", array);

      JSONArray dists = new JSONArray();
      for (String dist : distribution) {
        dists.put(dist);
      }
      json.put("dcat:distribution", dists);
      return json;
    } catch (JSONException e) {
      logger.error("", e);
      return new JSONObject();
    }
  }

  public Dataset() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public List<String> getKeyword() {
    return keyword;
  }

  public void setKeyword(List<String> keyword) {
    this.keyword = keyword;
  }

  public List<String> getDistribution() {
    return distribution;
  }

  public void setDistribution(List<String> distribution) {
    this.distribution = distribution;
  }

  public String getPortalParameter() {
    return portalParameter;
  }

  public void setPortalParameter(String portalParameter) {
    this.portalParameter = portalParameter;
  }

  public boolean isExposePublic() {
    return exposePublic;
  }

  public void setExposePublic(boolean exposePublic) {
    this.exposePublic = exposePublic;
  }

  // //dct:issued":"2014-09-15
  // public class DatasetCompare implements Comparator<Dataset>
  // {
  // public int compare(Dataset one, Dataset two)
  // {
  // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
  // try{
  // Date thisdate = sdf.parse(one.getIssued());
  // Date odate = sdf.parse(two.getIssued());
  // if (thisdate == null || odate == null)
  // return 0;
  // return thisdate.compareTo(odate);
  // }catch(Exception e){
  // return 0;
  // }
  // }
  // }

  public String getAccessURL() {
    return accessURL;
  }

  public void setAccessURL(String accessURL) {
    this.accessURL = accessURL;
  }

  public String getFileId() {
    return fileId;
  }

  public void setFileId(String fileId) {
    this.fileId = fileId;
  }
}
