package eu.dapaas.dao;

import org.json.JSONException;
import org.json.JSONObject;
//[{"enabled":true,"api_key":"s4edbehpr44r","secret":null}]

public class APIKey {
  private String  apiKey;
  private String  apiSecret;
  private boolean enable;
  
  public APIKey(JSONObject o) {
    try {
      this.apiKey = o.getString("api_key");
    } catch (JSONException e) {
      
    }
    try {
      this.apiSecret = o.getString("secret");
    } catch (JSONException e) {
      
    }
    try {
      this.enable = o.getBoolean("enabled");
    } catch (JSONException e) {
      
    }
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getApiSecret() {
    return apiSecret;
  }

  public void setApiSecret(String apiSecret) {
    this.apiSecret = apiSecret;
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }
}
