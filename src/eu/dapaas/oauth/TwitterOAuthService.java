package eu.dapaas.oauth;

import javax.servlet.http.HttpServletRequest;

import net.oauth.consumer.OAuth1Consumer;
import net.oauth.exception.OAuthException;
import net.oauth.signature.OAuthSignature;
import net.oauth.signature.impl.OAuthHmacSha1Signature;
import net.oauth.token.oauth1.AccessToken;
import net.oauth.token.oauth1.RequestToken;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.neurologic.oauth.config.ModuleConfig;
import com.neurologic.oauth.config.OAuthConfig;
import com.neurologic.oauth.providers.TwitterServiceProvider;
import com.neurologic.oauth.service.impl.OAuth1Service;

import eu.dapaas.constants.AuthenticationProvider;
import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.User;
import eu.dapaas.handler.UserHandler;
import eu.dapaas.http.HttpMethod;
import eu.dapaas.http.NameValuePair;
import eu.dapaas.http.impl.DaPaasGateway;
import eu.dapaas.http.impl.DaPaasParams;
import eu.dapaas.http.impl.DaPaasUserGateway;
import eu.dapaas.utils.Config;
import eu.dapaas.utils.Utils;

public class TwitterOAuthService extends OAuth1Service {
  private static final Logger logger                        = Logger.getLogger(TwitterOAuthService.class);
  private static final String GRAPH_API_BASIC               = "https://api.twitter.com/oauth/access_token";

  public static final String  TWITTER_REQUEST_TOKEN_SESSION = "TWITTER_REQUEST_TOKEN_SESSION";
  public static final String  TWITTER_ACCESS_TOKEN_SESSION  = "TWITTER_ACCESS_TOKEN_SESSION";

  // @Override
  protected AuthenticationProvider getProvider() {
    return AuthenticationProvider.twitter;
  }

  @Override
  public void saveAccessToken(HttpServletRequest request, AccessToken accessToken) {
    final String token = accessToken.getToken();
    logger.info("Got access token for " + getProvider() + " user: " + token);

    try {
      OAuthUserData userData = getOAuthUserData(accessToken, request);
      JSONObject userJason = new JSONObject();
      try {
        userJason.put("twitter_id", userData.getId());
      } catch (JSONException e) {
        logger.error("", e);
      }
      DaPaasParams params = new DaPaasParams();
      params.setJsonObject(new NameValuePair<JSONObject>(null, userJason));
      DaPaasUserGateway gateway = new DaPaasUserGateway(HttpMethod.PUT, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts/login"), params);
      JSONObject serverResponse = Utils.convertEntityToJSON(gateway.execute());
      if (serverResponse != null && serverResponse.has("error_message")) {
        try {
          userJason.put("name", userData.getName());
          userJason.put("email", "");
          userJason.put("role", userData.getRole());
        } catch (JSONException e) {
          logger.error("", e);
        }
        gateway.modifiedUserGateway(HttpMethod.POST, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts"), params);
        serverResponse = Utils.convertEntityToJSON(gateway.execute());
        logger.info(serverResponse);
        if (!Utils.isEmpty(serverResponse.getString("error_message"))){
          request.getSession().setAttribute(SessionConstants.ERROR, serverResponse.getString("error_message"));
          return;
        }
      }
      params = new DaPaasParams();
      params.setJsonObject(new NameValuePair<JSONObject>(null, userJason));
      params.getHeaders().put("Content-Type", "application/json");
      UserHandler userHandler = new UserHandler(gateway);
      User user = userHandler.getUserTempKey();
      request.getSession().setAttribute(SessionConstants.DAPAAS_USER, user);
      
    } catch (Throwable e) {
      logger.error(e.getMessage(), e);
    }
  }

  protected OAuthUserData getOAuthUserData( AccessToken token, HttpServletRequest request) throws Exception {
    
    OAuthUserData userData = new OAuthUserData();
    // userData.setEmail(email);
     userData.setId(token.getAdditionalParameters().get("user_id"));
     userData.setName(token.getAdditionalParameters().get("screen_name"));
     String role = (String) request.getSession().getAttribute(SessionConstants.CREATE_USER_ROLE);
     userData.setRole(role);
    return userData;
  }

  public static String getOAuthLoginUrl(ModuleConfig moduleConfig, HttpServletRequest request) throws OAuthException {
    OAuthConfig oauthConfig = moduleConfig.getOAuthConfigByName(AuthenticationProvider.twitter.name());
    String key = Config.getInstance().getString("oauth.client.id." + AuthenticationProvider.twitter);
    String secret = Config.getInstance().getString("oauth.client.secret." + AuthenticationProvider.twitter);
    String path = Config.getInstance().getString("oauth.path." + AuthenticationProvider.twitter);
    String role = request.getParameter("role");
    request.getSession().setAttribute(SessionConstants.CREATE_USER_ROLE, role);
    TwitterServiceProvider oauthProvider = new TwitterServiceProvider();
    OAuth1Consumer consumer = new OAuth1Consumer(key, secret, oauthProvider);
    consumer.getClient().addResponseHeader("Content-Type", "application/x-www-form-urlencoded");
    

    RequestToken requestToken = consumer.requestUnauthorizedToken(null, path, null, new OAuthHmacSha1Signature());
    request.getSession().setAttribute(TWITTER_REQUEST_TOKEN_SESSION, requestToken);
    
    
    String url = consumer.createOAuthUserAuthorizationUrl(requestToken, null);
    logger.info("Authorization url is  " + url);
    return url;

  }

  @Override
  protected String getRealm() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected OAuthSignature getOAuthSignature() {
    return new OAuthHmacSha1Signature();
  }

  @Override
  protected RequestToken getRequestToken(HttpServletRequest request) {
    return (RequestToken) request.getSession().getAttribute(TWITTER_REQUEST_TOKEN_SESSION);
  }

}
