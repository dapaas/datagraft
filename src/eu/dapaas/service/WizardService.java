package eu.dapaas.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sirmamobile.commlib.WebSessionObject;
import com.sirmamobile.commlib.annotations.WebMethod;
import com.sirmamobile.commlib.annotations.WebParam;
import com.sirmamobile.commlib.annotations.WebService;
import com.sirmamobile.commlib.annotations.WebSession;

import eu.dapaas.bean.Wizard;
import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.ErrorService;
import eu.dapaas.dao.PortalContent;
import eu.dapaas.dao.Transformation;
import eu.dapaas.dao.TransformationMeta;
import eu.dapaas.dao.User;
import eu.dapaas.handler.DatasetHandler;
import eu.dapaas.handler.TransformationCatalogHandler;
import eu.dapaas.handler.TransformationHandler;
import eu.dapaas.utils.Utils;

@WebService
public class WizardService {
  private static final Logger logger = Logger.getLogger(WizardService.class);

//  @WebMethod
//  public Object setup(
//	  	@WebParam(name = "step_number") Integer step,
//	  	@WebParam(name = "datasetname") String datasetName,
//	  	@WebParam(name = "description") String description,
//	  	@WebParam(name = "keyword") String keyword,
//	  	@WebParam(name = "licensing") String licensing,
//	  	@WebParam(name = "usagerights") String usagerights,
//	  	@WebParam(name = "bytesize") String bytesize,
//	  	@WebParam(name = "portalparam") String portalparam,
//	  	@WebSession WebSessionObject webSession) {
//
//    Wizard wizard = (Wizard) webSession.getSessionObject("wizard");
//    if (wizard == null) {
//      wizard = new Wizard();
//      wizard.setAction("new");
//      wizard.setType("dataset");
//    }
//
//    if (step == 1) {
//      if (datasetName != null && datasetName.length() > 0) {
//        wizard.getDetails().setTitle(datasetName);
//      }
//
//      if (description != null && description.length() > 0) {
//        wizard.getDetails().setDescription(description);
//      }
//
//      if (!Utils.isEmpty(keyword)) {
//        String[] kws = keyword.trim().split(",");
//        wizard.getDetails().setKeyword(new ArrayList<String>());
//        for (String k : kws) {
//          if (k.trim().length() > 0) {
//            wizard.getDetails().getKeyword().add(k);
//          }
//        }
//      }
//
//      if (licensing != null) {
//        wizard.getDetails().setLicensing(licensing);
//      }
//      if (usagerights != null) {
//        wizard.getDetails().setUsagerights(usagerights);
//      }
//      if (bytesize != null) {
//        wizard.getDetails().setBytesize(bytesize);
//      }
//    }
//
//    if (step == 4) {
//      wizard.getPortal().setParameter(portalparam);
//    }
//
//    webSession.putSessionObject("wizard", wizard);
//
//    return true;
//  }
//
//  @WebMethod
//  public Object finish(@WebRequest WebRequestObject request, @WebResponse WebResponseObject response, @WebSession WebSessionObject webSession) {
//    Wizard wizard = (Wizard) webSession.getSessionObject("wizard");
//    User user = (User) webSession.getSessionObject(SessionConstants.DAPAAS_USER);
//
//    if (wizard.getType().equals("dataset")) {
//      DatasetHandler handler = new DatasetHandler(wizard, user);
//      try {
//        if (wizard.getAction().equals("new")) {
//          handler.createDataset(false);
//        }
//        if (wizard.getAction().equals("edit")) {
//          handler.updateDataset();
//        }
//      } catch (Exception e) {
//        logger.error("", e);
//      }
//    }
//    webSession.putSessionObject("wizard", null);
//    // create all in API
//    return true;
//  }

  @WebMethod
  public Object getTransformations(@WebParam(name = "q") String searchValue, @WebSession WebSessionObject webSession) {
    User user = (User) webSession.getSessionObject(SessionConstants.DAPAAS_USER);
    TransformationCatalogHandler handler = new TransformationCatalogHandler(user.getApiKey(), user.getApiSecret());
    handler.setSearchValue(searchValue);
    List<Transformation> transformations = handler.getTransformationCatalog();
    return transformations;
  }

  @WebMethod
  public Object removeFileFromList(@WebParam(name = "index") String index, @WebSession WebSessionObject webSession) {
    Wizard wizard = (Wizard) webSession.getSessionObject("wizard");
    wizard.setUploadesFile(null);
    webSession.putSessionObject("wizard", wizard);
    return true;
  }

  @WebMethod
  public Object selectTransformation(@WebParam(name = "transformationid") String transformationId, @WebSession WebSessionObject webSession) {
    Wizard wizard = (Wizard) webSession.getSessionObject("wizard");
    wizard.getUploadesFile().setTransformation(transformationId);
    webSession.putSessionObject("wizard", wizard);
    return true;
  }

  @WebMethod
  public Object create(
		@WebParam(name = "datasetname") String datasetName,
		@WebParam(name = "description") String description,
		@WebParam(name = "keyword") String keyword,
		@WebParam(name = "licensing") String licensing,
		@WebParam(name = "usagerights") String usagerights,
		@WebParam(name = "bytesize") String bytesize,
		@WebParam(name = "portalparam") String portalparam,
		@WebParam(name = "portaltitle") String portaltitle,
		@WebParam(name = "filecontenttype") String filecontenttype,
		@WebParam(name = "public") Boolean isPublic,
		@WebParam(name="israw")String israw,
		@WebSession WebSessionObject webSession) {

    User user = (User) webSession.getSessionObject(SessionConstants.DAPAAS_USER);
    Wizard wizard = (Wizard) webSession.getSessionObject("wizard");
    if (wizard == null) {
      wizard = new Wizard();
      wizard.setAction("new");
      wizard.setType("dataset");
    }
    if (isPublic != null) {
      wizard.getDetails().setExposePublic(isPublic);
    }

    if (datasetName != null && datasetName.length() > 0) {
      wizard.getDetails().setTitle(datasetName);
    }


      wizard.getDetails().setDescription(description);

    if (filecontenttype != null && filecontenttype.length() > 0) {
      wizard.getUploadesFile().setContentType(filecontenttype);
    }
    if (!Utils.isEmpty(keyword)) {
      String[] kws = keyword.trim().split(",");
      wizard.getDetails().setKeyword(new ArrayList<String>());
      for (String k : kws) {
        if (k.trim().length() > 0) {
          wizard.getDetails().getKeyword().add(k);
        }
      }
    }else{
      wizard.getDetails().getKeyword().clear();
    }
      if (licensing != null) {
        wizard.getDetails().setLicensing(licensing);
      }
      if (usagerights != null) {
        wizard.getDetails().setUsagerights(usagerights);
      }
      if (bytesize != null) {
        wizard.getDetails().setBytesize(bytesize);
      }

    if (!Utils.isEmpty(portalparam))
      wizard.getPortal().setParameter(portalparam);
    if (!Utils.isEmpty(portaltitle))
      wizard.getPortal().setTitle(portaltitle);

    webSession.putSessionObject("wizard", wizard);
    if (wizard.getType().equals("dataset")) {
      DatasetHandler handler = new DatasetHandler(wizard, user);
      try {
        if (wizard.getAction().equals("new")) {
          wizard = handler.createDataset(!Utils.isEmpty(israw));
        }
        if (wizard.getAction().equals("edit")) {
          wizard = handler.updateDataset();
        }
      } catch (Exception e) {
        logger.error("", e);
      }
      if (!Utils.isEmpty(handler.getError())){
        return new ErrorService(handler.getError());
      }
    }

    // DatasetHandler
    wizard.setAction("edit");
    webSession.putSessionObject("wizard", wizard);
    // webSession.putSessionObject("wizard", null);
    return true;
  }
  
  @WebMethod
  public Object forkTransformation(@WebParam(name = "transformationId") String id, @WebSession WebSessionObject webSession){
    User user = (User) webSession.getSessionObject(SessionConstants.DAPAAS_USER);
    if (user == null){
      return null;
    }
    TransformationHandler header = new TransformationHandler(user.getApiKey(), user.getApiSecret());
    TransformationCatalogHandler handlercatalog = new TransformationCatalogHandler(user.getApiKey(), user.getApiSecret());
    Transformation transformation = handlercatalog.getDetail(id);
    TransformationMeta transformationMeta = new TransformationMeta();

    transformationMeta.setDescription(transformation.getDescription());
    transformationMeta.setPublic(transformation.isPublic());
    transformationMeta.setTitle(transformation.getTitle()+"-fork");
    transformationMeta.setTransformationCommand(transformation.getTransformationCommand());
    transformationMeta.setTransformationType(transformation.getTransformationType());

    File clojure = header.getClojureFile(id, user.getUsername());
    File json = header.getJsonFile(id, user.getUsername());
    return header.createTransformation(transformationMeta, clojure, json);
  }
  

  
  

  @WebMethod
  public Object addConfiguration(@WebParam(name = "portalpara") String portalparam,
      @WebParam(name = "query") String query,
      @WebParam(name = "drawtype") String drawtype,
      @WebParam(name = "title") String title,
      @WebParam(name = "description") String description,
      @WebParam(name = "summary") String summary,
      @WebParam(name = "datePattern") String datePattern,
      @WebSession WebSessionObject webSession) {

    Wizard wizard = (Wizard) webSession.getSessionObject("wizard");
    wizard.getPortal().setParameter(portalparam);
    PortalContent pc = new PortalContent();
    pc.setChart(drawtype);
    pc.setQuery(query);
    pc.setTitle(title);
    pc.setDescription(description);
    pc.setSummary(summary);
    pc.setDatePattern(datePattern);
    pc.setId("N" + wizard.getPortal().getPortalContent().size() + 1);

    pc.setDescriptionHtml(pc.getDescriptionHtml());
    pc.setSummaryHtml(pc.getSummaryHtml());
    pc.setQueryHtml(pc.getQueryHtml());
    pc.setChartLabel(pc.getChartLabel());

    wizard.getPortal().getPortalContent().add(pc);
    webSession.putSessionObject("wizard", wizard);
    return pc;
  }

  @WebMethod
  public Object deleteConfiguration(@WebParam(name = "portalpara") String portalparam,
      @WebParam(name = "portalid") String portalid,
      @WebSession WebSessionObject webSession) {

    Wizard wizard = (Wizard) webSession.getSessionObject("wizard");
    List<PortalContent> list = wizard.getPortal().getPortalContent();
    for (PortalContent pc : list) {
      if (pc.getId().equals(portalid)) {
        list.remove(pc);
        break;
      }
    }
    webSession.putSessionObject("wizard", wizard);
    return true;
  }

  @WebMethod
  public Object editConfiguration(@WebParam(name = "portalpara") String portalparam,
      @WebParam(name = "query") String query,
      @WebParam(name = "drawtype") String drawtype,
      @WebParam(name = "title") String title,
      @WebParam(name = "description") String description,
      @WebParam(name = "summary") String summary,
      @WebParam(name = "datePattern") String datePattern,
      @WebParam(name = "portalid") String portalid,
      @WebSession WebSessionObject webSession) {

    Wizard wizard = (Wizard) webSession.getSessionObject("wizard");
    for (PortalContent pc : wizard.getPortal().getPortalContent()) {
      if (pc.getId().equals(portalid)) {
        pc.setChart(drawtype);
        pc.setQuery(query);
        pc.setTitle(title);
        pc.setDescription(description);
        pc.setSummary(summary);
        pc.setDatePattern(datePattern);

        pc.setDescriptionHtml(pc.getDescriptionHtml());
        pc.setSummaryHtml(pc.getSummaryHtml());
        pc.setQueryHtml(pc.getQueryHtml());
        pc.setChartLabel(pc.getChartLabel());

        return pc;
      }
    }
    webSession.putSessionObject("wizard", wizard);
    return true;
  }
}
