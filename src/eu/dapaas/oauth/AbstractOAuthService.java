package eu.dapaas.oauth;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.oauth.token.oauth2.AccessToken;

import org.apache.http.cookie.Cookie;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.neurologic.oauth.service.impl.OAuth2Service;

import eu.dapaas.constants.AuthenticationProvider;
import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.User;
import eu.dapaas.handler.UserHandler;
import eu.dapaas.http.HttpMethod;
import eu.dapaas.http.NameValuePair;
import eu.dapaas.http.impl.DaPaasParams;
import eu.dapaas.http.impl.DaPaasUserGateway;
import eu.dapaas.utils.Config;
import eu.dapaas.utils.Utils;


public abstract class AbstractOAuthService extends OAuth2Service {
  
  private static final Logger logger = Logger.getLogger(AbstractOAuthService.class);
  
  @Override
  public void saveAccessToken(HttpServletRequest request, AccessToken accessToken) {
    final String token = accessToken.getAccessToken();
    logger.info("Got access token for " + getProvider() + " user: " + token);
    
    try {
      OAuthUserData userData = getOAuthUserData(token, request);
      // first try to login with id if exception create user 
      if (getProvider().equals(AuthenticationProvider.facebook)){
        JSONObject userJason = new JSONObject();
        try {
          userJason.put("facebook_id", userData.getId());
        } catch (JSONException e) {
          logger.error("", e);
        }
        DaPaasParams params = new DaPaasParams();
        params.setJsonObject(new NameValuePair<JSONObject>(null, userJason));
        DaPaasUserGateway gateway = new DaPaasUserGateway(HttpMethod.PUT, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts/login"), params);
        JSONObject serverResponse = Utils.convertEntityToJSON(gateway.execute());
        if (serverResponse != null && serverResponse.has("error_message")) {
          try {
            if (Utils.isEmpty(userData.getName())){
              userData.setName(userData.getEmail());
            }
            userJason.put("name", userData.getName());
            userJason.put("email", userData.getEmail());
            userJason.put("role", userData.getRole());
          } catch (JSONException e) {
            logger.error("", e);
          }
          gateway.modifiedUserGateway(HttpMethod.POST, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts"), params);
          serverResponse = Utils.convertEntityToJSON(gateway.execute());
          logger.info(serverResponse);
          if (serverResponse != null && serverResponse.has("error_message")) {
            request.getSession().setAttribute(SessionConstants.ERROR, serverResponse.getString("error_message"));
            return;
          }
        }
        List<Cookie> cookies = gateway.getContext().getCookieStore().getCookies();
        params = new DaPaasParams();
        params.setJsonObject(new NameValuePair<JSONObject>(null, userJason));
        params.getHeaders().put("Content-Type", "application/json");
        UserHandler userHandler = new UserHandler(gateway);
        User user = userHandler.getUserTempKey();
        user.setUsername(userData.getId());
        user.setCookies(cookies);
        user.setProvider(AuthenticationProvider.facebook);
        user.setProviderId(userData.getId());
        user.setConfirm(true);
        request.getSession().setAttribute(SessionConstants.DAPAAS_USER, user);
      }
      
      if (getProvider().equals(AuthenticationProvider.google)){
        JSONObject userJason = new JSONObject();
        try {
          userJason.put("google_id", userData.getId());
        } catch (JSONException e) {
          logger.error("", e);
        }
        DaPaasParams params = new DaPaasParams();
        params.setJsonObject(new NameValuePair<JSONObject>(null, userJason));
        DaPaasUserGateway gateway = new DaPaasUserGateway(HttpMethod.PUT, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts/login"), params);
        JSONObject serverResponse = Utils.convertEntityToJSON(gateway.execute());
        if (serverResponse != null && serverResponse.has("error_message")) {
          try {
            if (Utils.isEmpty(userData.getName())){
              userData.setName(userData.getEmail());
            }
            userJason.put("name", userData.getName());
            userJason.put("email", userData.getEmail());
            userJason.put("role", userData.getRole());
          } catch (JSONException e) {
            logger.error("", e);
          }
          gateway.modifiedUserGateway(HttpMethod.POST, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts"), params);
          serverResponse =  Utils.convertEntityToJSON(gateway.execute());
          logger.info(serverResponse);
          if (serverResponse != null && serverResponse.has("error_message")) {
            request.getSession().setAttribute(SessionConstants.ERROR, serverResponse.getString("error_message"));
            return;
          }
        }
        List<Cookie> cookies = gateway.getContext().getCookieStore().getCookies();
        params = new DaPaasParams();
        params.setJsonObject(new NameValuePair<JSONObject>(null, userJason));
        params.getHeaders().put("Content-Type", "application/json");
        UserHandler userHandler = new UserHandler(gateway);
        User user = userHandler.getUserTempKey();
        user.setUsername(userData.getId());
        user.setCookies(cookies);
        user.setProvider(AuthenticationProvider.google);
        user.setProviderId(userData.getId());
        user.setConfirm(true);
        if (Utils.isEmpty(user.getName())){
          user.setName(user.getEmail());
        }
        request.getSession().setAttribute(SessionConstants.DAPAAS_USER, user);
      }
    } catch (Throwable e) {
      logger.error(e.getMessage(), e);
    }
  }
  
  @Override
  protected String getRedirectUri() {
    return Config.getInstance().getString("oauth.path." + getProvider());
  }
  
  @Override
  protected String getState() {
    return null;
  }
  
  @Override
  protected String[] getScope() {
    String scope = Config.getInstance().getString("oauth.scope." + getProvider());
    return scope.split(getScopeDelimiter());
  }
  
  @Override
  protected String getScopeDelimiter() {
    return Config.getInstance().getString("oauth.scope.delimiter." + getProvider());
  }

  protected abstract AuthenticationProvider getProvider();
  
  protected abstract OAuthUserData getOAuthUserData(String token, HttpServletRequest request) throws Exception;
  
}
