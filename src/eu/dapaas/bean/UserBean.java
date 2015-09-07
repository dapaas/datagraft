package eu.dapaas.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.sirmamobile.commlib.annotations.WebParam;

import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.APIKey;
import eu.dapaas.dao.User;
import eu.dapaas.handler.LoginHandler;
import eu.dapaas.handler.LogoutHandler;
import eu.dapaas.handler.UserHandler;
import eu.dapaas.utils.Utils;

public class UserBean {
  private static final Logger logger = Logger.getLogger(UserBean.class);

  public void logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    LogoutHandler cathandler = new LogoutHandler(request, response, session);
    try {
      Cookie[]  cookies = request.getCookies();
      if (cookies != null)
        for (int i = 0; i < cookies.length; i++){
          if (cookies[i].getName().equals(SessionConstants.DATAAS_USER_NAME)){
             cookies[i].setValue(null);
             cookies[i].setMaxAge(0);
             cookies[i].setPath(request.getContextPath()+"/");
             response.addCookie(cookies[i]);
          }
        }
      cathandler.logout();
      session.invalidate();
    } catch (Exception e) {;
      logger.error("", e);
      session.invalidate();
    }
    try{
      response.sendRedirect(request.getContextPath() + "/pages/catalogs");
    }catch(IOException e){
      
    }
  }
  
  public List<APIKey> getAPIKeys(HttpServletRequest request, HttpServletResponse response, HttpSession session){
    try {
      // wait 3 sec
      try {
        Thread.sleep(2000);                 //1000 milliseconds is one second.
    } catch(InterruptedException ex) {
        Thread.currentThread().interrupt();
    }
      UserHandler cathandler = new UserHandler(request, response, session);
      List<APIKey> results = cathandler.getAPIKeys();
      return results;
    } catch (Exception e) {;
      logger.error("", e);
    }
    return new ArrayList<APIKey>();
  }
  
  public APIKey cretaeAPIKey(HttpServletRequest request, HttpServletResponse response, HttpSession session){
    try {
      UserHandler cathandler = new UserHandler(request, response, session);
      return cathandler.createAPIKeys();
      
    } catch (Exception e) {;
      logger.error("", e);
      return null;
    }
  }
  
  public void deleteAPIKey(HttpServletRequest request, HttpServletResponse response, HttpSession session, String apiKey){
    try {
      UserHandler cathandler = new UserHandler(request, response, session);
      cathandler.deleteAPIKeys(apiKey);
      
    } catch (Exception e) {;
      logger.error("", e);
    }
  }
  
  public void modifiedAPIKey(HttpServletRequest request, HttpServletResponse response, HttpSession session, String apiKey, boolean enable){
    try {
      UserHandler cathandler = new UserHandler(request, response, session);
      if (enable){
        cathandler.disableAPIKeys(apiKey);
      }else{
        cathandler.enableAPIKeys(apiKey);
      }
    } catch (Exception e) {;
      logger.error("", e);
    }
  }

  public String getTemporaryBasicAuth(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                      String usage) {
    String basicAuthKey = SessionConstants.DAPAAS_TMPKEY_PREFIX + usage;
    String basicAuthDateKey = SessionConstants.DAPAAS_TMPKEY_PREFIX + usage + "_date";

    String basicAuth = (String) session.getAttribute(basicAuthKey);
    Long basicAuthDate = (Long) session.getAttribute(basicAuthDateKey);

    Long now = new Date().getTime();

    // If the basic auth doesn't exist or is 24h old
    if (basicAuth == null || basicAuthDate == null || (now - basicAuthDate >= 24*60*60*1000)) {
      try {
        UserHandler cathandler = new UserHandler(request, response, session);
        User apiResponse = cathandler.getTempKey();
        basicAuth = apiResponse.getApiKey() + ":" + apiResponse.getApiSecret();
        session.setAttribute(basicAuthKey, basicAuth);
        session.setAttribute(basicAuthDateKey, now);
      } catch (Exception e) {
        logger.error("", e);
      }
    }

    return basicAuth;
  }

  public boolean loginStatus(HttpServletRequest request, HttpServletResponse response, HttpSession session){
    LoginHandler handler = new LoginHandler(request, response, request.getSession());
    return handler.loginStatus();
  }
  
  public User getDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    try {
      UserHandler cathandler = new UserHandler(request, response, session);
      return cathandler.getUserDetail();
      
    } catch (Exception e) {;
      logger.error("", e);
      return null;
    }
  }
  
  
  public void updateDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{
    try {
      UserHandler cathandler = new UserHandler(request, response, session);
      User userdetails = new User();
      userdetails.setUsername(request.getParameter("username"));
      userdetails.setName(request.getParameter("name"));
      userdetails.setEmail(request.getParameter("email"));
      
      cathandler.updateUserDetail(userdetails);
      
    } catch (Exception e) {
      logger.error("", e);
      return;
    }
  }
  
  public void putInCookie(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    if (session.getAttribute(SessionConstants.DAPAAS_USER) != null) {
      User user = (User) session.getAttribute(SessionConstants.DAPAAS_USER);
      Cookie cookie = new Cookie(SessionConstants.DATAAS_USER_LOGIN_EVER, user.getUsername());
      cookie.setMaxAge(60 * 60 * 24 * 360 * 10);
      cookie.setPath(request.getContextPath()+"/");
      response.addCookie(cookie);
    }
  }

  
  public void requestPasswordReset(String email)throws Exception{
    try{
    UserHandler handler = new UserHandler();
    handler.requestPasswordReset(email);
    }catch(Exception e){
      throw new Exception("Can't send request for reset password to this email. Please check youre email!");
    }
  }
  
  public void confirmPasswordReset(String email, String newpassword, String newconfirmpass, String token) throws Exception{
    String error = null;
    if (Utils.isEmpty(newconfirmpass) || Utils.isEmpty(newpassword)){
      error = "Cannot set empty password.";
      throw new Exception(error);
    }
    if (!newconfirmpass.equals(newpassword)){
      error = "Password confirmation does not match.";
      throw new Exception(error);
    }
    UserHandler handler = new UserHandler();
    handler.confirmPasswordReset(email, newpassword, token);
  }
}
