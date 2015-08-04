package eu.dapaas.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;

import eu.dapaas.dao.Dataset;
import eu.dapaas.handler.DatasetCatalogHandler;

public class DatasetAllBean {

  private HttpServletResponse response;
  private HttpSession         session;
  private String              searchValue;
  private String              owner;

  public List<Dataset> getCatalogDataset() throws JSONException, IOException {
    DatasetCatalogHandler cathandler = new DatasetCatalogHandler();
    cathandler.setSearchValue(searchValue);
    cathandler.setOwner(owner);
    return cathandler.getDatasetCatalog();
  }

  public List<Dataset> getCatalogSharedDataset() throws JSONException, IOException {
    DatasetCatalogHandler cathandler = new DatasetCatalogHandler();
    cathandler.setSearchValue(searchValue);
    cathandler.setOwner(owner);
    return cathandler.getSharedDatasetCatalog();
  }

  public List<Dataset> getCatalogDatasetPortals() throws JSONException, IOException {
    DatasetCatalogHandler cathandler = new DatasetCatalogHandler();
    cathandler.setSearchValue(searchValue);
    List<Dataset> datasets = cathandler.getSharedDatasetCatalog();
    List<Dataset> datasetsPortals = new ArrayList<Dataset>();
    for (Dataset uds : datasets) {
      if (uds.getPortalParameter() != null && uds.getPortalParameter().length() > 0) {
        datasetsPortals.add(uds);
      }
    }
    return datasetsPortals;
  }
  
  public Dataset getCatalogDetails(String datasetId) throws JSONException, IOException {
    DatasetCatalogHandler cathandler = new DatasetCatalogHandler();
    return cathandler.getDetailsCatalog(datasetId);
  }

  public String getSearchValue() {
    return searchValue;
  }

  public void setSearchValue(String searchValue) {
    this.searchValue = searchValue;
  }

  public HttpServletResponse getResponse() {
    return response;
  }

  public void setResponse(HttpServletResponse response) {
    this.response = response;
  }

  public HttpSession getSession() {
    return session;
  }

  public void setSession(HttpSession session) {
    this.session = session;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

}
