package eu.dapaas.bean;

import eu.dapaas.dao.Dataset;
import eu.dapaas.dao.Transformation;
import eu.dapaas.dao.UploadFile;
import eu.dapaas.dao.WizardPortal;

public class Wizard {
  private String         action       = "none";
  private String         type         = "dataset";
  private Dataset        details      = new Dataset();
  private UploadFile     uploadesFile = new UploadFile();
  private WizardPortal   portal       = new WizardPortal();
  private Transformation transformation = new Transformation();

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public UploadFile getUploadesFile() {
    return uploadesFile;
  }

  public void setUploadesFile(UploadFile uploadesFile) {
    this.uploadesFile = uploadesFile;
  }

  public Dataset getDetails() {
    return details;
  }

  public void setDetails(Dataset details) {
    this.details = details;
  }

  public void emptyNew() {
    action = "new";
    details = new Dataset();
    uploadesFile = null;
    portal = new WizardPortal();
    transformation = new Transformation();
  }
  
  public void emptyNone() {
    action = "none";
    details = new Dataset();
    uploadesFile = null;
    portal = new WizardPortal();
    transformation = new Transformation();
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public WizardPortal getPortal() {
    return portal;
  }

  public void setPortal(WizardPortal portal) {
    this.portal = portal;
  }

  public Transformation getTransformation() {
    return transformation;
  }

  public void setTransformation(Transformation transformation) {
    this.transformation = transformation;
  }
}
