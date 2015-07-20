package eu.dapaas.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import eu.dapaas.dao.Transformation;
import eu.dapaas.http.HttpMethod;
import eu.dapaas.http.impl.DaPaasGateway;
import eu.dapaas.http.impl.DaPaasParams;
import eu.dapaas.utils.Utils;

public class TransformationCatalogHandler extends BaseHandler {
  private static final Logger logger  = Logger.getLogger(TransformationCatalogHandler.class);
  private String              apiKey;
  private String              apiSecret;
  private String              searchValue;
  private DaPaasGateway       gateway = null;

  public TransformationCatalogHandler(String apiKey, String apiSecret) {
    this.apiKey = apiKey;
    this.apiSecret = apiSecret;
  }

  public TransformationCatalogHandler() {
    this.apiKey = null;
    this.apiSecret = null;
  }

  public List<Transformation> getTransformationCatalog() {
    List<Transformation> catalog = new ArrayList<Transformation>();
    try {
      JSONObject serverResponse = new JSONObject();
      if (searchValue != null && searchValue.length() > 0) {
        gateway = new DaPaasGateway(HttpMethod.GET, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/transformations/search?q=" + Utils.htmlEncoding(searchValue)));
        serverResponse = Utils.convertEntityToJSON(gateway.execute());
      } else {
        gateway = new DaPaasGateway(HttpMethod.GET, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/transformations/catalog"));
        serverResponse = Utils.convertEntityToJSON(gateway.execute());
      }
      logger.debug("CATALOG : " + serverResponse);
      JSONArray records = (JSONArray) serverResponse.get("dcat:record");
      if (records != null) {
        for (int i = 0; i < records.length(); i++) {
          catalog.add(new Transformation((JSONObject) records.get(i)));
        }
      }
      logger.info("get data for portal");
//      try {
//        Collections.sort(catalog, Collections.reverseOrder());
//      } catch (Throwable t) {
//        logger.error("", t);
//      }
    } catch (IOException e) {
      // showErrorPage(e.getMessage());
    } catch (Exception e) {
      logger.error("", e);
    }

    return catalog;
  }

  public List<Transformation> getSharedTransformationCatalog() {
    List<Transformation> catalog = new ArrayList<Transformation>();
    try {
      JSONObject serverResponse = new JSONObject();
      DaPaasParams params = new DaPaasParams();
      params.getHeaders().put("showShared", "y");
      if (searchValue != null && searchValue.length() > 0) {
        gateway = new DaPaasGateway(HttpMethod.GET, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/transformations/search?q=" + Utils.htmlEncoding(searchValue)), params);
        serverResponse = Utils.convertEntityToJSON(gateway.execute());
      } else {
        gateway = new DaPaasGateway(HttpMethod.GET, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/transformations/catalog"), params);
        serverResponse = Utils.convertEntityToJSON(gateway.execute());
      }
      logger.debug("CATALOG : " + serverResponse);
      JSONArray records = (JSONArray) serverResponse.get("dcat:record");
      if (records != null) {
        for (int i = 0; i < records.length(); i++) {
          catalog.add(new Transformation((JSONObject) records.get(i)));
        }
      }
      logger.info("get data for portal");
//      try {
//        Collections.sort(catalog, Collections.reverseOrder());
//      } catch (Throwable t) {
//        logger.error("", t);
//      }
    } catch (IOException e) {
      // showErrorPage(e.getMessage());
    } catch (Exception e) {
      logger.error("", e);
    }

    return catalog;
  }

  public Transformation getDetail(String transformationId) {
    try {
      // /transformations
      Transformation detail = new Transformation();
      JSONObject serverResponse = new JSONObject();
      DaPaasParams params = new DaPaasParams();
      params.getHeaders().put("transformation-id", transformationId.replace(" ", "+"));
      gateway = new DaPaasGateway(HttpMethod.GET, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/transformations"), params);
      serverResponse = Utils.convertEntityToJSON(gateway.execute());
      detail = new Transformation(serverResponse);
      logger.info("get data for portal");
      return detail;
    } catch (IOException e) {
      // showErrorPage(e.getMessage());
      return new Transformation();
    } catch (Exception e) {
      logger.error("", e);
      return new Transformation();
    }
  }

  public String getSearchValue() {
    return searchValue;
  }

  public void setSearchValue(String searchValue) {
    this.searchValue = searchValue;
  }
}
