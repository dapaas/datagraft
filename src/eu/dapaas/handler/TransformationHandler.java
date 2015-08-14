package eu.dapaas.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import eu.dapaas.dao.Transformation;
import eu.dapaas.dao.TransformationMeta;
import eu.dapaas.http.HttpMethod;
import eu.dapaas.http.NameValuePair;
import eu.dapaas.http.impl.DaPaasGateway;
import eu.dapaas.http.impl.DaPaasParams;
import eu.dapaas.utils.Utils;

public class TransformationHandler extends BaseHandler {

  private static final Logger logger  = Logger.getLogger(TransformationHandler.class);
  private DaPaasGateway       gateway = null;
  private String              apiKey;
  private String              apiSecret;

  public TransformationHandler(String apiKey, String apiSecret) {
    this.apiKey = apiKey;
    this.apiSecret = apiSecret;
  }

  // create
  public String createTransformation(TransformationMeta metadata, File clojure, File jsonfile) {
    try {
 //"meta=@transf/createTransformationMeta.json;type=application/ld+json" -F "tr-clojure=@transf/code.clj" -F "tr-json=@transf/code.json"
      DaPaasParams params = new DaPaasParams();
      metadata.setIssued(formatCurrentDate());
      metadata.setModified(formatCurrentDate());
      params.setMultipart(true);
      
      params.setFile(new NameValuePair<File>("tr-clojure", clojure));
      params.setFile(new NameValuePair<File>("tr-json", jsonfile));
       
      params.setJsonObject(new NameValuePair<JSONObject>("meta", metadata.toJSON()));

      gateway = new DaPaasGateway(HttpMethod.POST, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/transformations"), params);
      JSONObject response2 = Utils.convertEntityToJSON(gateway.execute());
      String transformationId = response2.get("@id").toString();
      logger.info("transformation ID: " + transformationId);
      return transformationId;
    } catch (Exception e) {
      logger.error("", e);
    }
    return null;
  }

  /*GET      /transformations/code/clojure    'transformation-id*/
  
  public File getClojureFile(String transformationId, String username){
    try {
      //"meta=@transf/createTransformationMeta.json;type=application/ld+json" -F "tr-clojure=@transf/code.clj" -F "tr-json=@transf/code.json"
      DaPaasParams params = new DaPaasParams();
      params.getHeaders().put("transformation-id", transformationId.replace(" ", "+"));
           gateway = new DaPaasGateway(HttpMethod.GET, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/transformations/code/clojure"), params);
           HttpResponse response = gateway.execute();
           String filename = username+"code.clj";
           File file = getFile(username, filename, response);
           logger.info("transformation ID: " + transformationId);
           return file;
         } catch (Exception e) {
           logger.error("", e);
         }
    return null;
  }
  
  /*GET      /transformations/code/json       'transformation-id*/
  
  public File getJsonFile(String transformationId, String username){
    try {
      DaPaasParams params = new DaPaasParams();
      params.getHeaders().put("transformation-id", transformationId.replace(" ", "+"));
           gateway = new DaPaasGateway(HttpMethod.GET, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/transformations/code/json"), params);
           HttpResponse response = gateway.execute();
           String filename = username+"code.json";
           File file = getFile(username, filename, response);
           logger.info("transformation ID: " + transformationId);
           return file;
         } catch (Exception e) {
           logger.error("", e);
         }
    return null;
  }
  
  public void deleteTransformation(String transformationId) {
    try {
      DaPaasParams params = new DaPaasParams();
      params.getHeaders().put("transformation-id", transformationId.replace(" ", "+"));
      gateway = new DaPaasGateway(HttpMethod.DELETE, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/transformations"), params);
      HttpResponse response = gateway.execute();
      logger.info("Delete transformation : " + response);
    } catch (Exception e) {
      logger.error(e);
    }
  }

  private String formatCurrentDate() {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date today = Calendar.getInstance().getTime();
    String reportDate = df.format(today);
    return reportDate;
  }
  
  private File getFile(String username, String filename, HttpResponse response) throws Exception{
    String tempFolder = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
    InputStream ins = null;
    OutputStream out = null;
    File tempfile = null;
    try {
      File dir = new File(tempFolder + File.separator + username);
      if (!dir.exists()) {
        dir.mkdir();
      }
      ins = response.getEntity().getContent();
      tempfile = new File(tempFolder + File.separator + username + File.separator + filename);
      if (tempfile.exists()) {
        tempfile.delete();
      }
      out = new FileOutputStream(tempfile);
      byte[] buf = new byte[1024];
      int len;
      while ((len = ins.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
      ins.close();
      out.close();
    } finally {
      if (ins != null)
        ins.close();
      if (out != null)
        out.close();
    }
    return tempfile;
  }
}
