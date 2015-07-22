package eu.dapaas.handler;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.APIKey;
import eu.dapaas.dao.User;
import eu.dapaas.http.HttpMethod;
import eu.dapaas.http.impl.DaPaasParams;
import eu.dapaas.http.impl.DaPaasUserGateway;
import eu.dapaas.utils.Utils;

public class UserHandler extends BaseHandler {
  private static final Logger logger  = Logger.getLogger(UserHandler.class);
  private DaPaasUserGateway   gateway = null;
  private HttpServletRequest  request;

  public UserHandler(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    setResponse(response);
    setSession(session);
    this.request = request;
  }

  public UserHandler(DaPaasUserGateway gateway) {
    this.gateway = gateway;
  }

  public User getUserTempKey() {
    try {
      DaPaasParams params = new DaPaasParams();
      params.getHeaders().put("Content-Type", "application/json");
      User user = new User();
      // get api key and secret
      if (gateway == null) {
        gateway = new DaPaasUserGateway(HttpMethod.POST, Utils.getDaPaasEndpoint("dapaas-management-services/api/api_keys/temporary"), params);
      } else {
        gateway.modifiedUserGateway(HttpMethod.POST, Utils.getDaPaasEndpoint("dapaas-management-services/api/api_keys/temporary"), params);
      }
      HttpResponse httpresponse = gateway.execute();
      JSONObject apiResponse = Utils.convertEntityToJSON(httpresponse);
      user.setUserApi(apiResponse);
      // get user details
      if (gateway == null) {
        gateway = new DaPaasUserGateway(HttpMethod.GET, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts/details"), params);
      } else {
        gateway.modifiedUserGateway(HttpMethod.GET, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts/details"), params);
      }
      httpresponse = gateway.execute();
      JSONObject userResponse = Utils.convertEntityToJSON(httpresponse);
      user.setUser(userResponse);
      return user;
    } catch (Exception e) {
      logger.error(e);
      return null;
    }
  }

  public User getTempKey() {
    try {
      DaPaasParams params = new DaPaasParams();
      params.getHeaders().put("Content-Type", "application/json");
      
      // get api key and secret
      User user = (User) request.getSession().getAttribute(SessionConstants.DAPAAS_USER);
      if (user == null){
        return null;
      }
      
      gateway = new DaPaasUserGateway(HttpMethod.POST, Utils.getDaPaasEndpoint("dapaas-management-services/api/api_keys/temporary"), params);
      for (Cookie cookie : user.getCookies()){
          gateway.getContext().getCookieStore().addCookie(cookie);
      }
      
      HttpResponse httpresponse = gateway.execute();
      JSONObject apiResponse = Utils.convertEntityToJSON(httpresponse);
      user.setUserApi(apiResponse);
      return user;
    } catch (Exception e) {
      logger.error(e);
      return null;
    }
  }
  //GET /api_keys/
  public List<APIKey> getAPIKeys(){
    try {
      List<APIKey> result = new ArrayList<APIKey>();
      DaPaasParams params = new DaPaasParams();
      params.getHeaders().put("Content-Type", "application/json");
      
      // get api key and secret
      User user = (User) request.getSession().getAttribute(SessionConstants.DAPAAS_USER);
      if (user == null){
        return new ArrayList<APIKey>();
      }
      gateway = new DaPaasUserGateway(HttpMethod.GET, Utils.getDaPaasEndpoint("dapaas-management-services/api/api_keys"), params);
      for (Cookie cookie : user.getCookies()){
        gateway.getContext().getCookieStore().addCookie(cookie);
      }
      
      HttpResponse httpresponse = gateway.execute();
      JSONArray apiResponse = Utils.convertToJSONArray(httpresponse);
      for (int i=0; i<apiResponse.length(); i++){
        JSONObject o = apiResponse.getJSONObject(i);
        result.add(new APIKey(o));
      }
      return result;
    } catch (Exception e) {
      logger.error(e);
      return new ArrayList<APIKey>();
    }
  }
  //POST /api_keys/
  public APIKey createAPIKeys(){
    try {
      DaPaasParams params = new DaPaasParams();
      params.getHeaders().put("Content-Type", "application/json");
      
      // get api key and secret
      User user = (User) request.getSession().getAttribute(SessionConstants.DAPAAS_USER);
      if (user == null){
        return null;
      }
      gateway = new DaPaasUserGateway(HttpMethod.POST, Utils.getDaPaasEndpoint("dapaas-management-services/api/api_keys"), params);
      for (Cookie cookie : user.getCookies()){
        gateway.getContext().getCookieStore().addCookie(cookie);
      }
      
      HttpResponse httpresponse = gateway.execute();
      APIKey key = new APIKey(Utils.convertEntityToJSON(httpresponse));
      key.setEnable(true);
      return key;
    } catch (Exception e) {
      logger.error(e);
    }
    return null;
  }
  
  // DELETE /api_keys/<api_key>
  public void deleteAPIKeys(String apiKey){
    try {
      DaPaasParams params = new DaPaasParams();
      params.getHeaders().put("Content-Type", "application/json");
      
      // get api key and secret
      User user = (User) request.getSession().getAttribute(SessionConstants.DAPAAS_USER);
      if (user == null){
        return;
      }
      gateway = new DaPaasUserGateway(HttpMethod.DELETE, Utils.getDaPaasEndpoint("dapaas-management-services/api/api_keys/"+apiKey), params);
      for (Cookie cookie : user.getCookies()){
        gateway.getContext().getCookieStore().addCookie(cookie);
      }
      
      HttpResponse httpresponse = gateway.execute();
    } catch (Exception e) {
      logger.error(e);
    }
  }
  
//PUT /api_keys/<api_key>/disable
 public void disableAPIKeys(String apiKey){
   try {
     DaPaasParams params = new DaPaasParams();
     params.getHeaders().put("Content-Type", "application/json");
     
     // get api key and secret
     User user = (User) request.getSession().getAttribute(SessionConstants.DAPAAS_USER);
     if (user == null){
       return;
     }
     gateway = new DaPaasUserGateway(HttpMethod.PUT, Utils.getDaPaasEndpoint("dapaas-management-services/api/api_keys/"+apiKey+"/disable"), params);
     for (Cookie cookie : user.getCookies()){
       gateway.getContext().getCookieStore().addCookie(cookie);
     }
     
     HttpResponse httpresponse = gateway.execute();
   } catch (Exception e) {
     logger.error(e);
   }
 }
 
//PUT /api_keys/<api_key>/enable
public void enableAPIKeys(String apiKey){
  try {
    DaPaasParams params = new DaPaasParams();
    params.getHeaders().put("Content-Type", "application/json");
    
    // get api key and secret
    User user = (User) request.getSession().getAttribute(SessionConstants.DAPAAS_USER);
    if (user == null){
      return;
    }
    gateway = new DaPaasUserGateway(HttpMethod.PUT, Utils.getDaPaasEndpoint("dapaas-management-services/api/api_keys/"+apiKey+"/enable"), params);
    for (Cookie cookie : user.getCookies()){
      gateway.getContext().getCookieStore().addCookie(cookie);
    }
    
    HttpResponse httpresponse = gateway.execute();
  } catch (Exception e) {
    logger.error(e);
  }
}
}
