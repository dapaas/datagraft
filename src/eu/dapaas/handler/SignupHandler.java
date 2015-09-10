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
import org.apache.http.cookie.Cookie;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import eu.dapaas.constants.AuthenticationProvider;
import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.User;
import eu.dapaas.http.HttpMethod;
import eu.dapaas.http.NameValuePair;
import eu.dapaas.http.impl.DaPaasGateway;
import eu.dapaas.http.impl.DaPaasParams;
import eu.dapaas.http.impl.DaPaasUserGateway;
import eu.dapaas.notification.EmailNotificationService;
import eu.dapaas.notification.impl.EmailVerificationMessage;
import eu.dapaas.utils.Utils;

/**
 * @author dzhelil
 * 
 */

public class SignupHandler extends BaseHandler {
  private static final Logger logger  = Logger.getLogger(SignupHandler.class);
  private DaPaasUserGateway   gateway = null;
  private HttpServletRequest  request;

  /*
   * SEND JSON: { "username" : "<login name>", "password" : "<pass>", "role" :
   * "<dapaas_role>", "name" : "<User name>", "email" : "<email>" }
   */
  public SignupHandler(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    setResponse(response);
    setSession(session);
    this.request = request;
  }

  public void singup() {

    JSONObject userJason = new JSONObject();
    String username = request.getParameter("username");
    String email = request.getParameter("email");
    String name = request.getParameter("name");
    try {
      userJason.put("username", username);
      userJason.put("password", request.getParameter("password"));
      userJason.put("role", request.getParameter("role")); // "data explorer"
      userJason.put("name", name);
      userJason.put("email", email);
    } catch (JSONException e) {
      logger.error("", e);
    }
    try {
      DaPaasParams params = new DaPaasParams();
      params.setJsonObject(new NameValuePair<JSONObject>(null, userJason));
      params.getHeaders().put("Content-Type", "application/json");
      gateway = new DaPaasUserGateway(HttpMethod.POST, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts"), params);
      HttpResponse httpresponse = gateway.execute();
      List<Cookie> cookies = gateway.getContext().getCookieStore().getCookies();
      JSONObject serverResponse = Utils.convertEntityToJSON(httpresponse);
      if (httpresponse != null) {

        logger.debug("serverResponse: " + serverResponse);
        if (serverResponse != null && serverResponse.has("error_message")) {
          try {
            showErrorPage(serverResponse.getString("error_message"));
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
          getSession().setAttribute(SessionConstants.DAPAAS_USER, user);
// send url to email 
          // EmailNotificationService.getInstance().sendNotificationAsync(new EmailVerificationMessage(email, username, name), email);
          redirectToPage("pages/publish", serverResponse);
        }
      } else {
        // {"error_message":"Email already in use"}
        showErrorPage("Server problem! Please, call to administrator.");
      }
    } catch (IOException e) {
      showErrorPage(e.getMessage());
    } catch (ServletException e) {
      showErrorPage(e.getMessage());
    }
  }
}