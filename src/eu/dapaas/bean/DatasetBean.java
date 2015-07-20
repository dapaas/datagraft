package eu.dapaas.bean;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;

import eu.dapaas.dao.Dataset;
import eu.dapaas.dao.Poligon;
import eu.dapaas.dao.PortalContent;
import eu.dapaas.dao.WizardPortal;
import eu.dapaas.db.LocalDBProvider;
import eu.dapaas.handler.DatasetCatalogHandler;

public class DatasetBean {

  private HttpServletResponse response;
  private HttpSession         session;
  private String              searchValue;

  public List<Dataset> getCatalogDataset(String apiKey, String apiSecret) throws JSONException, IOException {
    // get data from handler
    DatasetCatalogHandler cathandler = new DatasetCatalogHandler(apiKey, apiSecret);
    cathandler.setSearchValue(searchValue);
    return cathandler.getDatasetCatalog();
  }

  public List<Dataset> getCatalogSharedDataset(String apiKey, String apiSecret) throws JSONException, IOException {
    DatasetCatalogHandler cathandler = new DatasetCatalogHandler(apiKey, apiSecret);
    cathandler.setSearchValue(searchValue);
    return cathandler.getSharedDatasetCatalog();
  }

  public Dataset getCatalogDetails(String apiKey, String apiSecret, String datasetId) throws JSONException, IOException {
    DatasetCatalogHandler cathandler = new DatasetCatalogHandler(apiKey, apiSecret);
    return cathandler.getDetailsCatalog(datasetId);
  }

  public void catalogDelete(String apiKey, String apiSecret, String datasetId) throws JSONException, IOException {
    DatasetCatalogHandler cathandler = new DatasetCatalogHandler(apiKey, apiSecret);
    cathandler.deleteDatasetHandler(datasetId);
  }

  public Wizard getDatasetForEdit(Wizard wizard, String apiKey, String apiSecret, String datasetId) throws JSONException, IOException {
    if (datasetId != null) {
      Dataset details = getCatalogDetails(apiKey, apiSecret, datasetId);
      if (wizard == null) {
        wizard = new Wizard();
        wizard.setAction("edit");
        wizard.setType("dataset");
      }
      if (details != null) {
        wizard.setAction("edit");
        wizard.setDetails(details);

      }
//      // get portal from SQLite
      WizardPortal portal = LocalDBProvider.getPortaltByDatasetId(details.getId());
      wizard.setPortal(portal);
    }
    return wizard;
  }

  public Wizard addConfiguration(Wizard wizard, String portalParam, String portalTitle, String query, String drawType, String title, String description, String summary, String datePattern) {
    // Wizard wizard = (Wizard) session.getAttribute("wizard");
    wizard.getPortal().setParameter(portalParam);
    wizard.getPortal().setTitle(portalTitle);

    PortalContent pc = new PortalContent();
    pc.setChart(drawType);
    pc.setQuery(query);
    pc.setTitle(title);
    pc.setDescription(description);
    pc.setSummary(summary);
    pc.setDatePattern(datePattern);
    pc.setId("N" + wizard.getPortal().getPortalContent().size() + 1);
    wizard.getPortal().getPortalContent().add(pc);
    return wizard;
  }

  public void deleteConfiguration(Wizard wizard, String portalParam, String pcId) {

    wizard.getPortal().setParameter(portalParam);
    List<PortalContent> list = wizard.getPortal().getPortalContent();
    for (PortalContent pc : list) {
      if (pc.getId().equals(pcId)) {
        list.remove(pc);
        break;
      }
    }
  }

  public void editConfiguration(Wizard wizard, String portalParam, String portalTitle, String query, String drawType, String title, String description, String summary, String datePattern, String portalId) {

    for (PortalContent pc : wizard.getPortal().getPortalContent()) {
      if (pc.getId().equals(portalId)) {
        pc.setChart(drawType);
        pc.setQuery(query);
        pc.setTitle(title);
        pc.setDescription(description);
        pc.setSummary(summary);
        pc.setDatePattern(datePattern);
        // wizard.getPortal().getPortalContent().add(pc);
      }
    }
  }

  public WizardPortal getPortalByParameter(String paramportal) {
    // wizard.portal.title
    WizardPortal portal = LocalDBProvider.getPortaltByParam(paramportal);
    return portal;
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
  
  public List<Poligon> getPoligons(){
    return LocalDBProvider.getPoligons();
  }
}
