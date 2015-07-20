package eu.dapaas.utils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;

public class Config {
  private static final Logger logger   = Logger.getLogger(Config.class);

  private static final Config instance = new Config();

  public static Config getInstance() {
    return instance;
  }

  private Map<Locale, Properties>   localizedConfig = new HashMap<Locale, Properties>();
  private final ThreadLocal<Locale> threadLocale    = new ThreadLocal<Locale>();

  private Config() {
    try {
      loadConfig(null);
      for (Locale next : Locale.getAvailableLocales())
        loadConfig(next);
    } catch (IOException e) {
      logger.error("Failed to initialize Config class", e);
    }
  }

  public void setLocale(Locale locale) {
    threadLocale.set(locale);
  }

  private void loadConfig(Locale locale) throws IOException {
    String configFile = locale == null ? "config.properties" : "config_" + locale.getLanguage() + "_" + locale.getCountry().toLowerCase() + ".properties";
    URL resource = Loader.getResource(configFile);
    if (resource == null)
      return;
    Properties properties = new Properties();
    properties.load(resource.openStream());
    logger.info("Adding configuration for " + locale);
    localizedConfig.put(locale, properties);
  }

  public String getString(String s) {
    Properties properties = localizedConfig.get(threadLocale.get());
    properties = properties == null ? localizedConfig.get(null) : properties;
    return properties.getProperty(s);
  }

  public String getDaPaasServer() {
    return getString("dapaas.server");
  }

  public String getDaPaasUsername() {
    return getString("dapaas.username");
  }

  public String getDaPaasPassword() {
    return getString("dapaas.password");
  }

  public String getPathUploadFile() {
    return getString("path.upload.file");
  }

  public String getPortalURL() {
    return getString("portal.url");

  }

  public String getApplicationDeployment() {
    return getString("application.deploymant");

  }

  public String getDatasetPropertiesQuery() {
    return getString("dataset.properties.query");
  }

  /* dynamo, sqlite */
  public String getDapaasLocalDB() {
    return getString("dapaas.local.db");
  }

  /*
   * public String generateState() { String state = new BigInteger(130, new
   * SecureRandom()).toString(32); return state; }
   */

  public String getAmazonAccessKeyId() {
    return getString("amazon.access.key.id");
  }

  public String getAmazonSecretAccessKey() {
    return getString("amazon.secret.access.key");
  }
}
