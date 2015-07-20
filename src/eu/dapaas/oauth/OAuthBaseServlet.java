package eu.dapaas.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.consumer.OAuth1Consumer;
import net.oauth.exception.OAuthException;
import net.oauth.token.oauth1.RequestToken;

import org.apache.http.HttpEntity;
import org.apache.log4j.Logger;

import com.neurologic.oauth.config.ModuleConfig;
import com.neurologic.oauth.config.OAuthConfig;
import com.neurologic.oauth.config.ProviderConfig;
import com.neurologic.oauth.providers.TwitterServiceProvider;
import com.neurologic.oauth.servlet.OAuthServlet;
import com.neurologic.oauth.util.Globals;

import eu.dapaas.constants.AuthenticationProvider;
import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.User;
import eu.dapaas.http.HttpMethod;
import eu.dapaas.http.impl.FacebookGateway;
import eu.dapaas.utils.Config;
import eu.dapaas.utils.Utils;

/**
 * A callback servlet that is called by OAuth providers after user
 * authentication
 */
public class OAuthBaseServlet extends OAuthServlet {

  private static final Logger logger          = Logger.getLogger(OAuthBaseServlet.class);

  private static final String GRAPH_API_BASIC = "https://api.twitter.com/oauth/authenticate?oauth_token={0}";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {

      super.doGet(req, resp);
      if (req.getAttribute("error") != null)
        throw new Exception((String) req.getAttribute("error"));
     if (Utils.isEmpty((String) req.getSession().getAttribute(SessionConstants.ERROR))){
       handleSuccess(req, resp);
     }else{
       req.getRequestDispatcher("/pages/error.jsp").forward(req, resp);
     }
    } catch (Throwable e) {
      logger.error("", e);
      handleException(e, req, resp);
    }
  }

  private void handleSuccess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    if (req.getSession().getAttribute(SessionConstants.DAPAAS_USER) != null) {
      User user = (User) req.getSession().getAttribute(SessionConstants.DAPAAS_USER);
      if (user != null && user.getApiKey()!= null && user.getApiKey().length() > 0) {
        if (Utils.checkCatalog(user.getApiKey(), user.getApiSecret())) {
          resp.sendRedirect(req.getContextPath() + "/pages/myassets/index.jsp");
        } else {
          resp.sendRedirect(req.getContextPath() + "/pages/publish/index.jsp");
        }
      }
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      super.doPost(req, resp);
      if (req.getAttribute("error") != null)
        throw new Exception((String) req.getAttribute("error"));
      handleSuccess(req, resp);
    } catch (Throwable e) {
      handleException(e, req, resp);
      logger.error("", e);
    }
  }

  private void handleException(Throwable e, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    Throwable exc = e.getCause() != null ? e.getCause() : e;
    String oauthError = e.getMessage();
    String error = oauthError;
    request.getSession().setAttribute(SessionConstants.ERROR, error);
    request.getRequestDispatcher("/pages/error.jsp").forward(request, response);
  }
}
