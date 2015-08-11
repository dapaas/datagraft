package eu.dapaas.handler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
  private String              owner;
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
      HashMap<String, String> header = new HashMap<String, String>();
      if (!Utils.isEmpty(owner)){
        header.put("owner-filter", owner);
      }
      DaPaasParams params = new DaPaasParams();
      params.setHeaders(header);
      JSONObject serverResponse = new JSONObject();
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
      try {
        if (catalog.size() > 1) {
          Collections.sort(catalog, Collections.reverseOrder(new Comparator<Transformation>() {
            public int compare(Transformation one, Transformation two)
            {
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
              try {
                Date thisdate = null;
                Date odate = null;
                try{
                 thisdate = sdf.parse(one.getIssued());
                 
                }catch(Exception e){
                  
                }
                try{
                  
                  odate = sdf.parse(two.getIssued());
                 }catch(Exception e){
                   
                 }
                if (thisdate == null && odate == null)
                  return 0;
                if (thisdate == null && odate != null)
                  return -1;
                if (thisdate != null && odate == null)
                  return 1;
                return thisdate.compareTo(odate);
              } catch (Exception e) {
                return 0;
              }
            }
          }));
        }
      } catch (Throwable t) {
        logger.error("", t);
      }
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
      if (!Utils.isEmpty(owner)){
        params.getHeaders().put("owner-filter", owner);
      }
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
      try {
        if (catalog.size() > 1) {
          Collections.sort(catalog, Collections.reverseOrder(new Comparator<Transformation>() {
            public int compare(Transformation one, Transformation two)
            {
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
              try {
                Date thisdate = null;
                Date odate = null;
                try{
                 thisdate = sdf.parse(one.getIssued());
                }catch(Exception e){
                  
                }
                try{
                  odate = sdf.parse(two.getIssued());
                 }catch(Exception e){
                   
                 }
                if (thisdate == null && odate == null)
                  return 0;
                if (thisdate == null && odate != null)
                  return -1;
                if (thisdate != null && odate == null)
                  return 1;
                return thisdate.compareTo(odate);
              } catch (Exception e) {
                return 0;
              }
            }
          }));
        }
      } catch (Throwable t) {
        logger.error("", t);
      }
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

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }
}
