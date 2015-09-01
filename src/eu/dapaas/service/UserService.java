package eu.dapaas.service;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.sirmamobile.commlib.WebSessionObject;
import com.sirmamobile.commlib.annotations.WebMethod;
import com.sirmamobile.commlib.annotations.WebParam;
import com.sirmamobile.commlib.annotations.WebService;
import com.sirmamobile.commlib.annotations.WebSession;

import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.User;
import eu.dapaas.handler.UserHandler;
import eu.dapaas.http.HttpMethod;
import eu.dapaas.http.NameValuePair;
import eu.dapaas.http.impl.DaPaasParams;
import eu.dapaas.http.impl.DaPaasUserGateway;
import eu.dapaas.notification.EmailNotificationService;
import eu.dapaas.notification.impl.EmailVerificationMessage;
import eu.dapaas.utils.Utils;

@WebService
public class UserService {
  private static final Logger logger = Logger.getLogger(UserService.class);
  
  @WebMethod
  public Object changepassword(@WebParam(name="oldpass") String oldpass, @WebParam(name="newpass")String newpass, @WebParam(name="newconfirmpass")String newconfirmpass, @WebSession WebSessionObject webSession){
    try{
      User user = (User) webSession.getSessionObject(SessionConstants.DAPAAS_USER);
      if (user == null){
        return null;
      }
      String error = null;
      if (Utils.isEmpty(newconfirmpass) || Utils.isEmpty(newpass)){
        error = "Cannot set empty password.";
        return error;
      }
      if (!newconfirmpass.equals(newpass)){
        error = "Password confirmation does not match.";
        return error;
      }
      JSONObject userJason = new JSONObject();
      try {
        userJason.put("username", user.getUsername());
        userJason.put("password", oldpass);
      } catch (JSONException e) {
        logger.error("", e);
      }
      DaPaasParams params = new DaPaasParams();
      params.setJsonObject(new NameValuePair<JSONObject>(null, userJason));
      params.getHeaders().put("Content-Type", "application/json");
      DaPaasUserGateway gateway = new DaPaasUserGateway(HttpMethod.PUT, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts/login"), params);
      HttpResponse httpresponse = gateway.execute();
      JSONObject serverResponse = Utils.convertEntityToJSON(httpresponse);
      if (httpresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        if (serverResponse != null && serverResponse.has("error_message")) {
          error = "Invalid old password.";
          return error;
        }else{
          UserHandler handler = new UserHandler();
          handler.changePassword(user, newpass);
        }
      }
      
      if (error == null){
        return "OK";
      }else{
        return error;
      }
      
    }catch(Exception e){
      return false;
    }
  }
  
  @WebMethod
  public Object sendconfirmemail(@WebSession WebSessionObject webSession){
    try{
      User user = (User) webSession.getSessionObject(SessionConstants.DAPAAS_USER);
      if (user == null){
        return null;
      }
      EmailNotificationService.getInstance().sendNotificationAsync(new EmailVerificationMessage(user.getEmail(), user.getUsername(), user.getName()), user.getEmail());
      return true;
    }catch(Exception e){
      return false;
    }
  }
}
