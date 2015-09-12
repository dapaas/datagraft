package eu.dapaas.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.cookie.Cookie;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.sirmamobile.commlib.WebSessionObject;
import com.sirmamobile.commlib.annotations.WebMethod;
import com.sirmamobile.commlib.annotations.WebParam;
import com.sirmamobile.commlib.annotations.WebService;
import com.sirmamobile.commlib.annotations.WebSession;

import eu.dapaas.constants.AuthenticationProvider;
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
  
  @WebMethod
  public Object signup(@WebParam(name="username") String username,
      @WebParam(name="email") String email,
      @WebParam(name="name") String name,
      @WebParam(name="password") String password,
      @WebParam(name="role") String role,
      @WebSession WebSessionObject webSession) {

    String error = "";
    JSONObject userJason = new JSONObject();
    try {
      userJason.put("username", username);
      userJason.put("password", password);
      userJason.put("role", role); // "data explorer"
      userJason.put("name", name);
      userJason.put("email", email);
    } catch (JSONException e) {
      logger.error("", e);
    }
    try {
      DaPaasParams params = new DaPaasParams();
      params.setJsonObject(new NameValuePair<JSONObject>(null, userJason));
      params.getHeaders().put("Content-Type", "application/json");
      DaPaasUserGateway gateway = new DaPaasUserGateway(HttpMethod.POST, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts"), params);
      HttpResponse httpresponse = gateway.execute();
      List<Cookie> cookies = gateway.getContext().getCookieStore().getCookies();
      JSONObject serverResponse = Utils.convertEntityToJSON(httpresponse);
      if (httpresponse != null) {

        logger.debug("serverResponse: " + serverResponse);
        if (serverResponse != null && serverResponse.has("error_message")) {
          try {
            error = (serverResponse.getString("error_message"));
          } catch (JSONException e) {
            logger.error("", e);
          }
        } else {
          UserHandler userHandler = new UserHandler(gateway);
          User user = userHandler.getUserTempKey();
          user.setCookies(cookies);
          user.setProvider(AuthenticationProvider.dapaas);
          user.setProviderId(username);
          user.setEmail(email);
          webSession.putSessionObject(SessionConstants.DAPAAS_USER, user);
//          redirectToPage("pages/publish", serverResponse);
          return "OK";
        }
      } else {
        error = ("Server problem! Please, call to administrator.");
      }
      
    } catch (IOException e) {
      error = (e.getMessage());
    } 
    return error;
  }
}
