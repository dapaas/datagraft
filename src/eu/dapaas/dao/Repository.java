package eu.dapaas.dao;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

//{"access-url":"http://54.76.140.62:3080/openrdf-sesame/repositories/1505271111_statements"}
public class Repository {
  private static final Logger logger = Logger.getLogger(Repository.class);
  private String              accessURL;

  public Repository(JSONObject o) {
    try {
      this.accessURL = o.getString("access-url");
    } catch (JSONException e) {
      logger.error("", e);
    }

  }

  public String getAccessURL() {
    return accessURL;
  }

  public void setAccessURL(String accessURL) {
    this.accessURL = accessURL;
  }
}
