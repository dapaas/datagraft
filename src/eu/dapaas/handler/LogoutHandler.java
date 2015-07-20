/**
 * 
 */
package eu.dapaas.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.cookie.Cookie;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.User;
import eu.dapaas.http.HttpMethod;
import eu.dapaas.http.impl.DaPaasGateway;
import eu.dapaas.http.impl.DaPaasParams;
import eu.dapaas.http.impl.DaPaasUserGateway;
import eu.dapaas.utils.Utils;

/**
 * @author dzhelil
 * 
 */
public class LogoutHandler extends BaseHandler {

  private static final Logger logger = Logger.getLogger(LogoutHandler.class);
  private DaPaasUserGateway       gateway = null;
  private HttpServletRequest request;

  public LogoutHandler(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    setResponse(response);
    setSession(session);
    this.request = request;
  }

  public void logout() throws Exception {
    try {
      DaPaasParams params = new DaPaasParams();
      User user = (User) request.getSession().getAttribute(SessionConstants.DAPAAS_USER);
      if (user == null){
        return;
      }
      gateway = new DaPaasUserGateway(HttpMethod.PUT, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts/logout"), params);
      for (Cookie cookie : user.getCookies()){
        gateway.getContext().getCookieStore().addCookie(cookie);
      }
      JSONObject serverResponse = Utils.convertEntityToJSON(gateway.execute());
      logger.debug(serverResponse);
      
    } catch (IOException e) {
      showErrorPage(e.getMessage());
    }
//    redirectToPage("catalogs/index.jsp", new JSONObject());
  }

}
