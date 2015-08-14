package eu.dapaas.dao;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/*{"@context":{"dcat":"http://www.w3.org/ns/dcat#", 
 * "dct":"http://purl.org/dc/terms/",
 * "dct:issued":{"@type":"xsd:date"},
 * "dct:modified":{"@type":"xsd:date"}},
 "@type":"dcat:Transformation",
 "dct:issued":"2014-08-18",
 "dct:modified":"2014-08-17",
 "dct:title":"Sample Transformation from Graftwerk",
 "dct:description":"Description for Transformation",
 "dcat:public":"true",
 "dcat:transformationType":"graft",
 "dcat:transformationCommand":"my-graft"
 }*/
public class TransformationMeta {
  private static final Logger logger = Logger.getLogger(TransformationMeta.class);

  private String              id;
  private String              issued;
  private String              modified;
  private String              title;
  private String              description;
  private boolean             isPublic;
  private String              transformationType;
  private String              transformationCommand;

  public TransformationMeta(){
    
  }
  
  public TransformationMeta(JSONObject o) {
    try {
      this.id = o.getString("@id");
    } catch (JSONException e) {
      // e.printStackTrace();
    }
    try {
      this.issued = o.getString("dct:issued");
    } catch (JSONException e) {
      // e.printStackTrace();
      try {
        this.id = o.getString("foaf:primaryTopic");
      } catch (JSONException ee) {

      }
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
      this.isPublic = o.getBoolean("dct:public");
    } catch (JSONException e) {
      // e.printStackTrace();
    }

    try {
      this.transformationType = o.getString("dcat:transformationType");
    } catch (JSONException e) {

    }
    try {
      this.transformationCommand = o.getString("dcat:transformationCommand");
    } catch (JSONException e) {

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
      
//      jsoncontext.put("dcat","http://www.w3.org/ns/dcat#");
//      jsoncontext.put("foaf:primaryTopic", jsonid);
//      jsoncontext.put("foaf","http://xmlns.com/foaf/0.1/");
//      jsoncontext.put("xsd","http://www.w3.org/2001/XMLSchema#");
      
      
      JSONObject json = new JSONObject();
      json.put("@context", jsoncontext);
      json.put("@type", "dcat:Transformation");
      json.put("@id", this.id);
      json.put("dct:issued", this.issued);
      json.put("dct:modified", this.modified);
      json.put("dct:title", this.title);
      json.put("dct:description", this.description);
      json.put("dct:public", this.isPublic);
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
}
