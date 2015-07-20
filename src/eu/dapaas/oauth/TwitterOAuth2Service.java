package eu.dapaas.oauth;

import javax.servlet.http.HttpServletRequest;

import net.oauth.consumer.OAuth2Consumer;
import net.oauth.enums.GrantType;
import net.oauth.exception.OAuthException;
import net.oauth.parameters.OAuth2Parameters;
import net.oauth.provider.OAuth2ServiceProvider;
import net.oauth.token.oauth2.AccessToken;

import org.apache.log4j.Logger;

import com.neurologic.oauth.config.ModuleConfig;
import com.neurologic.oauth.config.OAuthConfig;
import com.neurologic.oauth.config.ProviderConfig;
import com.neurologic.oauth.service.impl.OAuth2Service;

import eu.dapaas.constants.AuthenticationProvider;
import eu.dapaas.utils.Config;

public class TwitterOAuth2Service extends OAuth2Service {
  private static final Logger logger            = Logger.getLogger(TwitterOAuth2Service.class);

  @Override
  public void saveAccessToken(HttpServletRequest request, AccessToken accessToken) {
    // TODO Auto-generated method stub

  }

  @Override
  protected String getRedirectUri() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String getState() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String[] getScope() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String getScopeDelimiter() {
    // TODO Auto-generated method stub
    return null;
  }
  
  public static String getOAuthLoginUrl(ModuleConfig moduleConfig, HttpServletRequest request) throws OAuthException {
    OAuthConfig oauthConfig = moduleConfig.getOAuthConfigByName(AuthenticationProvider.twitter.name());
    ProviderConfig providerConfig = oauthConfig.getProvider();
    String key = Config.getInstance().getString("oauth.client.id." + AuthenticationProvider.twitter);
    String secret = Config.getInstance().getString("oauth.client.secret." + AuthenticationProvider.twitter);
    String path = Config.getInstance().getString("oauth.path." + AuthenticationProvider.twitter);
    String scope = Config.getInstance().getString("oauth.scope." + AuthenticationProvider.twitter);
    String delimiter = Config.getInstance().getString("oauth.scope.delimiter." + AuthenticationProvider.twitter);
    
    String authUrl = providerConfig.getAuthorizationUrl();
    String tokenUrl = providerConfig.getAccessTokenUrl();
    
    OAuth2ServiceProvider oauthProvider = new OAuth2ServiceProvider(authUrl, tokenUrl);
    OAuth2Consumer consumer = new OAuth2Consumer(key, secret, oauthProvider);
    consumer.getClient().addResponseHeader("Content-Type", "application/x-www-form-urlencoded");
    
    OAuth2Parameters parameters = new OAuth2Parameters();
    parameters.setClientId(key);
    parameters.setClientSecret(secret);
    parameters.setGrantType("client_credentials");
    AccessToken token = consumer.requestAcessToken(GrantType.CLIENT_CREDENTIALS, parameters);
//    request.getSession().setAttribute(TWITTER_REQUEST_TOKEN_SESSION, requestToken);

    // String url = consumer.createOAuthUserAuthorizationUrl(requestToken, null);
    //logger.info("Authorization url is  " + url);
    return "";

  }
  

}
