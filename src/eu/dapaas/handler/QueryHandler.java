package eu.dapaas.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import eu.dapaas.dao.Dataset;
import eu.dapaas.dao.DistributionDetail;
import eu.dapaas.http.HttpMethod;
import eu.dapaas.http.impl.AccessGateway;
import eu.dapaas.http.impl.DaPaasGateway;
import eu.dapaas.http.impl.DaPaasParams;
import eu.dapaas.http.impl.DaPaasUserGateway;
import eu.dapaas.utils.Utils;

public class QueryHandler extends BaseHandler {
  private static final Logger logger = Logger.getLogger(QueryHandler.class);
  private String              query;
  private DaPaasGateway       gateway;

  public QueryHandler(String query) {
    this.query = query;

  }

  public QueryHandler() {
    super();
  }

  public String executeQueryById(String id) {
    return executeQueryById(null, null, id, null);
  }

  public String executeQueryById(String apiKey, String apiSecret, String id) {
    return executeQueryById(apiKey, apiSecret, id, null);
  }

  public String executeQueryById(String apiKey, String apiSecret, String id, String accept) {
    try {
      JSONObject serverResponse = new JSONObject();
      DaPaasParams params = new DaPaasParams();
      params.getHeaders().put("dataset-id", id);
      gateway = new DaPaasGateway(HttpMethod.GET, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/datasets"), params);
      serverResponse = Utils.convertEntityToJSON(gateway.execute());
      Dataset datasetCatalogDetails = new Dataset(serverResponse);
      String distibutionId = datasetCatalogDetails.getDistribution().get(0).toString();
      params = new DaPaasParams();
      params.getHeaders().put("distrib-id", distibutionId);
      gateway.modifiedDaPaasGateway(HttpMethod.GET, Utils.getDaPaasEndpoint("catalog/distributions"), params);
      JSONObject response3 = Utils.convertEntityToJSON(gateway.execute());
      DistributionDetail distribution = new DistributionDetail(response3);

      String accessURL = distribution.getAccessURL();
      if (accept == null) {
        accept = "application/json";
      }
      // accessURL = "http://factforge.net/sparql.json";
      accessURL = accessURL + "?query=" + Utils.htmlEncoding(query);
      params = new DaPaasParams();
      params.getHeaders().put("Accept", accept);
      AccessGateway gateway = new AccessGateway(HttpMethod.GET, apiKey, apiSecret, (accessURL), params);
      HttpResponse response = gateway.execute();
      String responseStr = Utils.convertEntityToString(response.getEntity());
      return responseStr;
    } catch (IOException e) {
      logger.error("", e);
      // showErrorPage(e.getMessage());
      return null;
    } catch (Exception e) {
      logger.error("", e);
      return null;
    }
  }

  public String executeQueryByUrl(String accessURL) {
    try {
      // accessURL = "http://factforge.net/sparql.json";
      accessURL = accessURL + "?query=" + Utils.htmlEncoding(query);
      AccessGateway gateway = new AccessGateway(HttpMethod.GET, (accessURL));
      HttpResponse response = gateway.execute();
      String responseStr = Utils.convertEntityToString(response.getEntity());
      return responseStr;
    } catch (IOException e) {
      logger.error("", e);
      return null;
    } catch (Exception e) {
      logger.error("", e);
      return null;
    }
  }

  public File exportRDF(String apiKey, String apiSecret, String username, String id, String contenttype) {
    try {
      JSONObject serverResponse = new JSONObject();
      DaPaasParams params = new DaPaasParams();
      params.getHeaders().put("dataset-id", id);
      gateway = new DaPaasGateway(HttpMethod.GET, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/datasets"), params);
      serverResponse = Utils.convertEntityToJSON(gateway.execute());
      Dataset datasetCatalogDetails = new Dataset(serverResponse);
      String distibutionId = datasetCatalogDetails.getDistribution().get(0).toString();
      params = new DaPaasParams();
      params.getHeaders().put("distrib-id", distibutionId);
      gateway.modifiedDaPaasGateway(HttpMethod.GET, Utils.getDaPaasEndpoint("catalog/distributions"), params);
      JSONObject response3 = Utils.convertEntityToJSON(gateway.execute());
      DistributionDetail distribution = new DistributionDetail(response3);
      params = new DaPaasParams();
      params.getHeaders().put("Accept", contenttype);
      String accessURL = distribution.getAccessURL();
      AccessGateway gateway = new AccessGateway(HttpMethod.GET, apiKey, apiSecret, (accessURL + "/statements"), params);
      HttpResponse response = gateway.execute();
      String filename = "statements";
      Header header = response.getFirstHeader("Content-Disposition");
      HeaderElement[] hes = header.getElements();
      for (HeaderElement he : hes) {
        NameValuePair nvp = he.getParameterByName("filename");
        if (nvp != null) {
          filename = nvp.getValue();
        }
      }
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
    } catch (Exception e) {
      logger.error("", e);
      return null;
    }
  }

//  public String executeQueryByUrl(String accessURL) {
//    try {
//      DaPaasParams params = new DaPaasParams();
//      params.getHeaders().put("Content-Type", "application/json");
//      DaPaasUserGateway gateway = new DaPaasUserGateway(HttpMethod.GET, Utils.getDaPaasEndpoint("dapaas-management-services/api/api_keys/temporary"), params);
//      HttpResponse httpresponse = gateway.execute();
//      JSONObject apiResponse = Utils.convertEntityToJSON(httpresponse);
//      String apiKey = null;
//      String apiSecret = null;
//      try {
//        apiKey = apiResponse.get("api_key").toString();
//        apiSecret = apiResponse.get("secret").toString();
//      } catch (JSONException je) {
//        je.printStackTrace();
//      }
//
//      return executeQueryByUrl(apiKey, apiSecret, accessURL);
//    } catch (Exception e) {
//      logger.error("", e);
//      return null;
//    }
//
//  }
}
