package eu.dapaas.handler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import eu.dapaas.dao.Dataset;
import eu.dapaas.dao.DistributionDetail;
import eu.dapaas.dao.WizardPortal;
import eu.dapaas.db.LocalDBProvider;
import eu.dapaas.http.HttpMethod;
import eu.dapaas.http.NameValuePair;
import eu.dapaas.http.impl.DaPaasGateway;
import eu.dapaas.http.impl.DaPaasParams;
import eu.dapaas.utils.Utils;

public class DatasetCatalogHandler extends BaseHandler {
  private static final Logger logger  = Logger.getLogger(DatasetCatalogHandler.class);
  private String              apiKey;
  private String              apiSecret;
  private String              searchValue;
  private String              owner;
  private DaPaasGateway       gateway = null;

  public DatasetCatalogHandler(String apiKey, String apiSecret) {
    this.apiKey = apiKey;
    this.apiSecret = apiSecret;
  }

  public DatasetCatalogHandler() {
    this.apiKey = null;
    this.apiSecret = null;
  }

  public List<Dataset> getDatasetCatalog() {
    List<Dataset> catalog = new ArrayList<Dataset>();
    try {
      
      HashMap<String, String> header = new HashMap<String, String>();
      if (!Utils.isEmpty(owner)){
        header.put("owner-filter", owner);
      }
      DaPaasParams params = new DaPaasParams();
      params.setHeaders(header);
      
      JSONObject serverResponse = new JSONObject();
      if (searchValue != null && searchValue.length() > 0) {
        gateway = new DaPaasGateway(HttpMethod.GET, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/datasets/search?q=" + Utils.htmlEncoding(searchValue)), params);
        serverResponse = Utils.convertEntityToJSON(gateway.execute());
      } else {
        gateway = new DaPaasGateway(HttpMethod.GET, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/datasets/catalog"), params);
        HttpResponse result = gateway.execute();
        serverResponse = Utils.convertEntityToJSON(result);
      }

      logger.debug("CATALOG : " + serverResponse);
      JSONArray records = (JSONArray) serverResponse.get("dcat:record");
      if (records != null) {
        for (int i = 0; i < records.length(); i++) {
          catalog.add(new Dataset((JSONObject) records.get(i)));
        }
      }
      logger.info("get data for portal");
      for (Dataset udc : catalog) {
        WizardPortal portal = LocalDBProvider.getPortaltByDatasetId(udc.getId());
        udc.setPortalParameter(portal.getParameter());
      }
      try {
        if (catalog.size() > 1) {
          Collections.sort(catalog, Collections.reverseOrder(new Comparator<Dataset>() {
            public int compare(Dataset one, Dataset two)
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

  // showShared=y
  public List<Dataset> getSharedDatasetCatalog() {
    List<Dataset> catalogShared = new ArrayList<Dataset>();
    try {

      HashMap<String, String> header = new HashMap<String, String>();
      header.put("showShared", "y");
      if (!Utils.isEmpty(owner)){
        header.put("owner-filter", owner);
      }

      JSONObject serverResponse = new JSONObject();
      if (searchValue != null && searchValue.length() > 0) {
        DaPaasParams params = new DaPaasParams();
        params.setHeaders(header);
        gateway = new DaPaasGateway(HttpMethod.GET, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/datasets/search?q=" + Utils.htmlEncoding(searchValue)), params);
      } else {
        DaPaasParams params = new DaPaasParams();
        params.setHeaders(header);
        gateway = new DaPaasGateway(HttpMethod.GET, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/datasets/catalog"), params);
      }
      serverResponse = Utils.convertEntityToJSON(gateway.execute());
      logger.debug("CATALOG : " + serverResponse);
      JSONArray records = (JSONArray) serverResponse.get("dcat:record");
      if (records != null) {
        for (int i = 0; i < records.length(); i++) {
          catalogShared.add(new Dataset((JSONObject) records.get(i)));
        }
      }
      try {

        Collections.sort(catalogShared, Collections.reverseOrder(new Comparator<Dataset>() {
          public int compare(Dataset one, Dataset two)
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
      } catch (Throwable t) {
        logger.error("", t);
      }
      logger.info("get data for portal");
      for (Dataset udc : catalogShared) {
        WizardPortal portal = LocalDBProvider.getPortaltByDatasetId(udc.getId());
        udc.setPortalParameter(portal.getParameter());
      }
    } catch (IOException e) {
      // showErrorPage(e.getMessage());
    } catch (Exception e) {
      logger.error("", e);
    }
    return catalogShared;
  }

  public Dataset getDetailsCatalog(String datasetId) {
    try {
      Dataset detail = new Dataset();
      JSONObject serverResponse = new JSONObject();
      // ?id=" +
      DaPaasParams params = new DaPaasParams();
      params.getHeaders().put("dataset-id", datasetId);
      gateway = new DaPaasGateway(HttpMethod.GET, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/datasets"), params);
      serverResponse = Utils.convertEntityToJSON(gateway.execute());
      detail = new Dataset(serverResponse);
      logger.info("get data for portal");

      JSONObject jemptycontext = new JSONObject();
      jemptycontext.put("@context", new JSONObject());
      
      if (detail.getDistribution().size()>0){
        String distibutionId = detail.getDistribution().get(0).toString();
        params = new DaPaasParams();
        params.setJsonObject(new NameValuePair<JSONObject>(null, jemptycontext));
        params.getHeaders().put("distrib-id", distibutionId);
        gateway.modifiedDaPaasGateway(HttpMethod.GET, Utils.getDaPaasEndpoint("catalog/distributions"), params);
        JSONObject response3 = Utils.convertEntityToJSON(gateway.execute());
  
        logger.debug("GET distributions : " + response3);
        DistributionDetail distribution = new DistributionDetail(response3);
        detail.setFileId(distribution.getFileId());
        String accessURL = distribution.getAccessURL();
        detail.setAccessURL(accessURL);
      }
      WizardPortal portal = LocalDBProvider.getPortaltByDatasetId(detail.getId());
      detail.setPortalParameter(portal.getParameter());

      return detail;
    } catch (IOException e) {
      // showErrorPage(e.getMessage());
      return new Dataset();
    } catch (Exception e) {
      logger.error("", e);
      return new Dataset();
    }
  }

  public void deleteDatasetHandler(String datasetId) {
    try {
      //

      Dataset dataset = getDetailsCatalog(datasetId);
      for (String distrId : dataset.getDistribution()) {
        DaPaasParams params = new DaPaasParams();
        params.getHeaders().put("distrib-id", distrId);
        
        gateway = new DaPaasGateway(HttpMethod.DELETE, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/distributions/repository"), params);
        JSONObject serverResponse = Utils.convertEntityToJSON(gateway.execute());
        logger.debug("Repository DELETE RESULT: " + serverResponse);
        
        
        gateway = new DaPaasGateway(HttpMethod.DELETE, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/distributions"), params);
        serverResponse = Utils.convertEntityToJSON(gateway.execute());
        logger.debug("Distribution DELETE RESULT: " + serverResponse);
      }
      DaPaasParams params = new DaPaasParams();
      params.getHeaders().put("dataset-id", datasetId);
      gateway = new DaPaasGateway(HttpMethod.DELETE, apiKey, apiSecret, Utils.getDaPaasEndpoint("catalog/datasets"), params);
      JSONObject serverResponse = Utils.convertEntityToJSON(gateway.execute());
      logger.debug("CATALOG DELETE RESULT: " + serverResponse);
      if (serverResponse != null && serverResponse.has("error_message")) {
        // showErrorPage(serverResponse.getString("error_message"));
      }
      // delete portal
      LocalDBProvider.deleteDSPortal(datasetId);
    } catch (IOException e) {
      // showErrorPage(e.getMessage());
    } catch (Exception e) {
      logger.error("", e);
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
