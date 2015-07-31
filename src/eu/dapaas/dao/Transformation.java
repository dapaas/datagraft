package eu.dapaas.dao;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.sirmamobile.commlib.annotations.MappedObject;
import com.sirmamobile.commlib.annotations.Oneway;
import com.sirmamobile.commlib.annotations.SkipMapping;

@Oneway
@MappedObject(recursive = true, forced = true)
public class Transformation {
  /*
   * {"@context":{ "dct:issued":{"@type":"xsd:date"},
   * "dcat":"http://www.w3.org/ns/dcat#", "foaf:primaryTopic":{"@type":"@id"},
   * "foaf":"http://xmlns.com/foaf/0.1/", "dcat:distribution":{"@type":"@id"},
   * "dct":"http://purl.org/dc/terms/",
   * "xsd":"http://www.w3.org/2001/XMLSchema#",
   * "dct:modified":{"@type":"xsd:date"} }, "dct:title":"lori demo",
   * "dct:jsonDataID":"1d118045-00f8-4b92-8c85-59173067a227.json",
   * "@type":"dcat:Transformation", "dct:modified":"2015-06-19",
   * "dct:publisher":"pinokio", "http://purl.org/dc/terms/public":"false",
   * "dct:clojureDataID":"71fe416f-a956-4985-8a7b-88a4ad211db8.clj",
   * "dct:description":"",
   * "http://www.w3.org/ns/dcat#transformationCommand":"my-pipe",
   * "dcat:keyword":[], "http://www.w3.org/ns/dcat#transformationType":"pipe",
   * "@id":"http://dapaas.eu/users/1505271111/transformation/lori+demo-1",
   * "dct:public":false }
   * 
   * 
   * ---------- for catalog "@type":"dcat:CatalogRecord", // for catalog
   * "dct:issued":"2014-08-18", "dct:publisher":"pinokio", "dcat:public":
   * "true","dct:title":"Title for Transformation", "foaf:primaryTopic":
   * "http://dapaas.eu/users/1505271111/transformation/Title+for+Transformation-1"
   * , "dct:modified":"2014-08-17"
   * 
   * 
   * 
   * }
   */
  @SkipMapping
  private static final Logger logger = Logger.getLogger(Transformation.class);

  private String              id;
  private String              issued;
  private String              modified;
  private String              title;
  private String              description;
  private boolean             isPublic;
  private String              transformationType;
  private String              transformationCommand;
  private String              jsonDataID;
  private String              clojureDataID;
  private String              publisher;

  public Transformation() {

  }

  public Transformation(JSONObject o) {
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
      // logger.error("", e);
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
      this.isPublic = o.getBoolean("dcat:public");
    } catch (JSONException e) {
      // e.printStackTrace();
    }

    try {
      this.transformationType = o.getString("http://www.w3.org/ns/dcat#transformationType");
    } catch (JSONException e) {

    }
    try {
      this.transformationCommand = o.getString("http://www.w3.org/ns/dcat#transformationCommand");
    } catch (JSONException e) {

    }
    //
    try {
      this.clojureDataID = o.getString("dct:clojureDataID");
    } catch (JSONException e) {

    }
    try {
      this.jsonDataID = o.getString("dct:jsonDataID");
    } catch (JSONException e) {

    }
    try {
      this.publisher = o.getString("dct:publisher");
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
      jsoncontext.put("dct", "http://purl.org/dc/terms/");
      jsoncontext.put("dct:issued", jsondate);
      jsoncontext.put("dct:modified", jsondate);

      jsoncontext.put("dcat", "http://www.w3.org/ns/dcat#");
      jsoncontext.put("foaf:primaryTopic", jsonid);
      jsoncontext.put("foaf", "http://xmlns.com/foaf/0.1/");
      jsoncontext.put("xsd", "http://www.w3.org/2001/XMLSchema#");

      JSONObject json = new JSONObject();
      json.put("@context", jsoncontext);
      json.put("@type", "dcat:Transformation");
      json.put("@id", this.id);
      json.put("dct:issued", this.issued);
      json.put("dct:modified", this.modified);
      json.put("dct:title", this.title);
      json.put("dct:description", this.description);
      json.put("dcat:public", this.isPublic);
      json.put("dcat:transformationCommand", this.transformationCommand);
      json.put("dcat:transformationType", this.transformationType);

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

  public boolean isPublic() {
    return isPublic;
  }

  public void setPublic(boolean isPublic) {
    this.isPublic = isPublic;
  }

  public String getTransformationType() {
    return transformationType;
  }

  public void setTransformationType(String transformationType) {
    this.transformationType = transformationType;
  }

  public String getTransformationCommand() {
    return transformationCommand;
  }

  public void setTransformationCommand(String transformationCommand) {
    this.transformationCommand = transformationCommand;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

}
