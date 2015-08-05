/**
 * 
 */
package eu.dapaas.handler;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.cookie.Cookie;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.User;
import eu.dapaas.http.HttpMethod;
import eu.dapaas.http.NameValuePair;
import eu.dapaas.http.impl.DaPaasParams;
import eu.dapaas.http.impl.DaPaasUserGateway;
import eu.dapaas.utils.Utils;

/**
 * @author dzhelil
 * 
 */
public class LoginHandler extends BaseHandler {
  private static final Logger logger  = Logger.getLogger(LoginHandler.class);
  private DaPaasUserGateway   gateway = null;
  private HttpServletRequest  request;

  public LoginHandler(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    setResponse(response);
    setSession(session);
    this.request = request;
  }

  public void login(String username, String password) {

    JSONObject userJason = new JSONObject();
    try {
      userJason.put("username", username);
      userJason.put("password", password);
    } catch (JSONException e) {
      logger.error("", e);
    }
    try {
      DaPaasParams params = new DaPaasParams();
      params.setJsonObject(new NameValuePair<JSONObject>(null, userJason));
      params.getHeaders().put("Content-Type", "application/json");
      gateway = new DaPaasUserGateway(HttpMethod.PUT, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts/login"), params);
      HttpResponse httpresponse = gateway.execute();
      JSONObject serverResponse = Utils.convertEntityToJSON(httpresponse);
      if (httpresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        logger.debug("serverResponse: " + serverResponse);

        List<Cookie> cookies = gateway.getContext().getCookieStore().getCookies();

        boolean isLogin = loginStatus();
        if (!isLogin) {
          showErrorPage("User is not Sign In. Please Sign In!");
          return;
        }
        // if user login det details
        if (serverResponse != null && serverResponse.has("error_message")) {
          showErrorPage(serverResponse.getString("error_message"));
        } else {
          UserHandler userHandler = new UserHandler(gateway);
          User user = userHandler.getUserTempKey();
          user.setCookies(cookies);
          request.getSession().setAttribute(SessionConstants.DAPAAS_USER, user);
          if (Utils.checkCatalog(user.getApiKey(), user.getApiSecret())) {
            redirectToPage("pages/myassets", serverResponse);
          } else {
            redirectToPage("pages/publish", serverResponse);
          }
        }
      } else {
        if (serverResponse != null && serverResponse.has("error_message")) {
          showErrorPage(serverResponse.getString("error_message"));
        } else {
          showErrorPage("Invalid login or password.");
        }
      }
    } catch (IOException e) {
      showErrorPage(e.getMessage());
    } catch (ServletException e) {
      showErrorPage(e.getMessage());
    } catch (JSONException je) {
      je.printStackTrace();
      showErrorPage(je.getMessage());
    }finally{
      // gateway.close();
    }
  }

  public boolean loginStatus() {
    try {
      DaPaasParams params = new DaPaasParams();
      params.getHeaders().put("Content-Type", "application/json");
      if (gateway == null) {
        User user = (User) request.getSession().getAttribute(SessionConstants.DAPAAS_USER);
        if (user == null){
          return false;
        }
        gateway = new DaPaasUserGateway(HttpMethod.GET, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts/login_status"), params);
        for (Cookie cookie : user.getCookies()){
          gateway.getContext().getCookieStore().addCookie(cookie);
        }
      } else {
        gateway.modifiedUserGateway(HttpMethod.GET, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts/login_status"), params);
      }
      HttpResponse httpresponse = gateway.execute();
      logger.debug("test httpresponse: " + httpresponse);
      if (httpresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        JSONObject loginstatus = Utils.convertEntityToJSON(httpresponse);
        String status = loginstatus.getString("status");
        if ("AUTHENTICATED".equals(status)) {
          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    } catch (Throwable t) {
      logger.error(t);
      return false;
    }
  }

}