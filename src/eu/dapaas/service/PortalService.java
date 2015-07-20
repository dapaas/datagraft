package eu.dapaas.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.json.JSONObject;

import com.sirmamobile.commlib.WebSessionObject;
import com.sirmamobile.commlib.annotations.WebMethod;
import com.sirmamobile.commlib.annotations.WebParam;
import com.sirmamobile.commlib.annotations.WebService;
import com.sirmamobile.commlib.annotations.WebSession;

import eu.dapaas.bean.Wizard;
import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.Poligon;
import eu.dapaas.dao.PortalContent;
import eu.dapaas.dao.SPARQLResult;
import eu.dapaas.dao.User;
import eu.dapaas.dao.WizardPortal;
import eu.dapaas.db.LocalDBProvider;
import eu.dapaas.handler.QueryHandler;
import eu.dapaas.utils.Config;
import eu.dapaas.utils.Utils;

@WebService
public class PortalService {

  @WebMethod
  public Object chatrdata(@WebParam(name = "param") String param, @WebParam(name = "chartId") String portalId, @WebSession WebSessionObject webSession) {
try{
    SPARQLResult sparqlresult = new SPARQLResult();
   
      if (portalId != null) {
        // get s
        PortalContent portalContent = LocalDBProvider.getPortalContent(portalId) ;
        QueryHandler hendler = new QueryHandler(portalContent.getQuery());
        String responseStr = hendler.executeQueryByUrl(portalContent.getAccessURL());
        String poligonStr = "";
        if (portalContent.getPoligon() != null && portalContent.getPoligon().getId() != null){
          File file = new File(Config.getInstance().getPathUploadFile()+File.separator+portalContent.getPoligon().getFilename());
          InputStream in = new FileInputStream(file);
          BufferedReader reader = new BufferedReader(new InputStreamReader(in));
          String line;
          while ((line = reader.readLine()) != null) {
            poligonStr = poligonStr + (line);
          }
          reader.close();
        }
        JSONObject qresult = Utils.convertStringToJSON(responseStr);
        sparqlresult = new SPARQLResult(qresult);
        sparqlresult.setColumns(Utils.convertColumn(responseStr));
        sparqlresult.setPoligon(Utils.convertStringToJSON(poligonStr));
      }
    return sparqlresult;
}catch(Exception e){
  e.printStackTrace();
	return new SPARQLResult();
}
  }
  
  @WebMethod
  public Object getcharts(@WebParam(name = "param") String param, @WebSession WebSessionObject webSession) {
    WizardPortal portal = LocalDBProvider.getPortaltByParam(param);
    List<PortalContent> contents = portal.getPortalContent();
    return contents;
  }

  @WebMethod
  public Object executeQuery(@WebParam(name="query") String query, @WebParam(name="poligonId") String poligonId, @WebSession WebSessionObject webSession){
	  try{
    Wizard wizard = (Wizard) webSession.getSessionObject("wizard");
    User user = (User) webSession.getSessionObject(SessionConstants.DAPAAS_USER);
    QueryHandler queryhandler = new QueryHandler(query);
    String responseStr = queryhandler.executeQueryById(user.getApiKey(),user.getApiSecret(), wizard.getDetails().getId());
    
    String poligonStr = "";
    if (poligonId != null && !Utils.isEmpty(poligonId)){
      Poligon poligon = LocalDBProvider.getPoligon(poligonId);
      File file = new File(Config.getInstance().getPathUploadFile()+File.separator+poligon.getFilename());
      InputStream in = new FileInputStream(file);
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = reader.readLine()) != null) {
        poligonStr = poligonStr + (line);
      }
      reader.close();
    }

    JSONObject qresult = Utils.convertStringToJSON(responseStr);
    SPARQLResult sparqlresult = new SPARQLResult(qresult);
    sparqlresult.setColumns(Utils.convertColumn(responseStr));
    sparqlresult.setPoligon(Utils.convertStringToJSON(poligonStr));
    return sparqlresult;
	  }catch(Exception e){
	    e.printStackTrace();
			return new SPARQLResult();
		}
  }
  
  
}
