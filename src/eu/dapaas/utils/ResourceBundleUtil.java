package eu.dapaas.utils;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class ResourceBundleUtil {
  private static Logger      logger                   = Logger.getLogger(ResourceBundleUtil.class);
  public static final String RESOURCE_BASE_MAILS      = "resources.email";
  
  private static ResourceBundle getBundle(String baseName) {
    return ResourceBundle.getBundle(baseName);
  }
  
  private static String getString(String baseName, String key, Object... arguments) {
    try {
      String value = getBundle(baseName).getString(key);
      return MessageFormat.format(value, arguments);
    } catch (MissingResourceException e) {
      // logger.error("Exception:" + e.getMessage());
    } 
    return key;
  }
  
  public static String getMailContent(String key, Object... arguments) {
    return getString(RESOURCE_BASE_MAILS, key, arguments);
  }
}
