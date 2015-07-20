package eu.dapaas.dao;

import java.util.ArrayList;
import java.util.List;

public class WizardPortal {
  private String              datasetId;
  private String              parameter;
  private String              title;
  private List<PortalContent> portalContent = new ArrayList<PortalContent>();

  public String getDatasetId() {
    return datasetId;
  }

  public void setDatasetId(String datasetId) {
    this.datasetId = datasetId;
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  public List<PortalContent> getPortalContent() {
    return portalContent;
  }

  public void setPortalContent(List<PortalContent> portalContent) {
    this.portalContent = portalContent;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}
