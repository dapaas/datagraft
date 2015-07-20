package eu.dapaas.oauth;

import java.io.InputStream;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import net.oauth.consumer.OAuth2Consumer;
import net.oauth.enums.ResponseType;
import net.oauth.exception.OAuthException;
import net.oauth.http.impl.ApacheHttpClient;
import net.oauth.provider.OAuth2ServiceProvider;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.neurologic.oauth.config.ModuleConfig;
import com.neurologic.oauth.config.OAuthConfig;
import com.neurologic.oauth.config.ProviderConfig;

import eu.dapaas.constants.AuthenticationProvider;
import eu.dapaas.constants.SessionConstants;
import eu.dapaas.http.HttpMethod;
import eu.dapaas.http.impl.FacebookGateway;
import eu.dapaas.utils.Config;

public class GoogleOAuthService extends AbstractOAuthService {
  private static final Logger logger          = Logger.getLogger(GoogleOAuthService.class);
  private static final String GOOGLE_APIS_URL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token={0}";

  @Override
  protected AuthenticationProvider getProvider() {
    return AuthenticationProvider.google;
  }

  @Override
  protected OAuthUserData getOAuthUserData(String token, HttpServletRequest request) throws Exception {
    FacebookGateway fbGateway = new FacebookGateway(HttpMethod.GET, MessageFormat.format(GOOGLE_APIS_URL, token));
    InputStream userInfo = fbGateway.execute().getEntity().getContent();
    JSONObject info = new JSONObject(IOUtils.toString(userInfo));
    final String id = info.getString("id");
    String email = info.getString("email");
    String name = info.getString("name");
    String role = (String) request.getSession().getAttribute(SessionConstants.CREATE_USER_ROLE);
    OAuthUserData userData = new OAuthUserData();
    userData.setEmail(email);
    userData.setId(id);
    userData.setName(name);
    userData.setRole(role);
    return userData;
  }

  public static String getOAuthLoginUrl(ModuleConfig moduleConfig, HttpServletRequest request) throws OAuthException {
    OAuthConfig oauthConfig = moduleConfig.getOAuthConfigByName(AuthenticationProvider.google.name());
    ProviderConfig providerConfig = oauthConfig.getProvider();
    String key = Config.getInstance().getString("oauth.client.id." + AuthenticationProvider.google);
    String secret = Config.getInstance().getString("oauth.client.secret." + AuthenticationProvider.google);
    String path = Config.getInstance().getString("oauth.path." + AuthenticationProvider.google);
    String scope = Config.getInstance().getString("oauth.scope." + AuthenticationProvider.google);
    String delimiter = Config.getInstance().getString("oauth.scope.delimiter." + AuthenticationProvider.google);

    String authUrl = providerConfig.getAuthorizationUrl();
    String tokenUrl = providerConfig.getAccessTokenUrl();
    
    String role = request.getParameter("role");
    request.getSession().setAttribute(SessionConstants.CREATE_USER_ROLE, role);

    OAuth2ServiceProvider oauthProvider = new OAuth2ServiceProvider(authUrl, tokenUrl);
    OAuth2Consumer consumer = new OAuth2Consumer(new ApacheHttpClient(), key, secret, oauthProvider);

    String url = consumer.generateRequestAuthorizationUrl(ResponseType.CODE, path, null, delimiter, scope.split(delimiter));
    logger.info("Authorization url is  " + url);
    return url;
  }
}
