package eu.dapaas.handler;

import java.io.File;
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
  public Transformation createTransformation(TransformationMeta metadata, File clojure) {
    try {
      // {"@context":{}}
      /*
       * {"@context":{"dcat":"http://www.w3.org/ns/dcat#",
       * "dct":"http://purl.org/dc/terms/"
       * ,"dct:issued":{"@type":"xsd:date"},"dct:modified"
       * :{"@type":"xsd:date"}}, "@type":"dcat:Transformation",
       * "dct:issued":"2014-08-18", "dct:modified":"2014-08-17",
       * "dct:title":"Sample Transformation from Graftwerk",
       * "dct:description":"Description for Transformation",
       * "dcat:public":"true", "dcat:transformationType":"graft",
       * "dcat:transformationCommand":"my-graft" }
       * 
       * curl -X POST -F
       * "meta=@transf/createTransformationMeta.json;type=application/ld+json"
       * -F "tr-clojure=@transf/code.clj" -F "tr-json=@transf/code.json"
       * http://ec2
       * -54-76-140-62.eu-west-1.compute.amazonaws.com:8080/catalog/transformations
       */

      // String transformationId = "";

      DaPaasParams params = new DaPaasParams();
      // params.getHeaders().put("Content-Type", "multipart/mixed; boundary=&");
      // TransformationMeta metadata = new TransformationMeta(new JSONObject());
      metadata.setIssued(formatCurrentDate());
      metadata.setModified(formatCurrentDate());
      params.setMultipart(true);
      params.setFile(new NameValuePair<File>("tr-clojure", clojure));
      params.setJsonObject(new NameValuePair<JSONObject>("meta", metadata.toJSON()));

      gateway = new DaPaasGateway(HttpMethod.POST, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/transformations"), params);
      JSONObject response2 = Utils.convertEntityToJSON(gateway.execute());
      String transformationId = response2.get("@id").toString();
      logger.info("transformation ID: " + transformationId);

    } catch (Exception e) {
      logger.error("", e);
    }
    return new Transformation();
  }

  // update

  // delete
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
}
