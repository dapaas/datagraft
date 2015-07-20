package eu.dapaas.http.impl;

import java.io.File;
import java.util.HashMap;

import org.json.JSONObject;

import eu.dapaas.http.NameValuePair;

public class DaPaasParams {

  private HashMap<String, String>   headers    = new HashMap<String, String>();

  private NameValuePair<File>       file       = null;
  private NameValuePair<String>     filename   = null;
  private NameValuePair<JSONObject> jsonObject = null;

  private boolean                   multipart;

  // private String contentType;

  // private String publisher = null;

  public NameValuePair<JSONObject> getJsonObject() {
    return jsonObject;
  }

  public void setJsonObject(NameValuePair<JSONObject> jsonObject) {
    this.jsonObject = jsonObject;
  }

  public HashMap<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(HashMap<String, String> headers) {
    this.headers = headers;
  }

  public NameValuePair<File> getFile() {
    return file;
  }

  public void setFile(NameValuePair<File> file) {
    this.file = file;
  }

  public NameValuePair<String> getFilename() {
    return filename;
  }

  public void setFilename(NameValuePair<String> filename) {
    this.filename = filename;
  }

  public boolean isMultipart() {
    return multipart;
  }

  public void setMultipart(boolean multypart) {
    this.multipart = multypart;
  }

  // public String getContentType() {
  // return contentType;
  // }
  //
  // public void setContentType(String contextType) {
  // this.contentType = contextType;
  // }
}
