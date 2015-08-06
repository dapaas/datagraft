package eu.dapaas.service;

import org.json.JSONObject;

import com.sirmamobile.commlib.WebSessionObject;
import com.sirmamobile.commlib.annotations.WebMethod;
import com.sirmamobile.commlib.annotations.WebParam;
import com.sirmamobile.commlib.annotations.WebService;
import com.sirmamobile.commlib.annotations.WebSession;

import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.SPARQLResult;
import eu.dapaas.dao.User;
import eu.dapaas.handler.QueryHandler;
import eu.dapaas.utils.Utils;

@WebService
public class QueryResultService {

  @WebMethod
  public Object getResultData(@WebParam(name = "query") String query, @WebParam(name = "id") String id, @WebSession WebSessionObject webSession) {
    try {
      User user = (User) webSession.getSessionObject(SessionConstants.DAPAAS_USER);
      SPARQLResult sparqlresult = new SPARQLResult();
      QueryHandler queryhandler = new QueryHandler(query);
      String apiKey = null;
      String apiSecret = null;
      if (user!=null && !Utils.isEmpty(user.getApiKey())){
        apiKey = user.getApiKey();
      }
      if (user!=null && !Utils.isEmpty(user.getApiSecret())){
        apiSecret = user.getApiSecret();
      }
      String responseStr = queryhandler.executeQueryById(apiKey, apiSecret, id);

      JSONObject qresult = Utils.convertStringToJSON(responseStr);
      sparqlresult = new SPARQLResult(qresult);
      sparqlresult.setColumns(Utils.convertColumn(responseStr));

      return sparqlresult;
    } catch (Exception e) {
      return new SPARQLResult();
    }
  }
}
