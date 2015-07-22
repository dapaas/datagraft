/**
 * 
 */
package eu.dapaas.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import eu.dapaas.dao.Dataset;
import eu.dapaas.handler.DatasetCatalogHandler;

/**
 * @author dzhelil
 * 
 */
public class Utils {
  private static final Logger logger = Logger.getLogger(Utils.class);

  public static boolean isEmpty(String s) {
    if (s == null) {
      return true;
    }
    if ((s.trim()).length() == 0) {
      return true;
    }
    return false;
  }

  public static JSONObject convertEntityToJSON(HttpResponse response) throws IOException {
    JSONObject json = new JSONObject();
    BufferedReader reader = null;
    HttpEntity entity = response.getEntity();
    try {
      reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
      StringBuilder builder = new StringBuilder();
      for (String line = null; (line = reader.readLine()) != null;) {
        builder.append(line).append("\n");
      }
      if (builder.indexOf("{", 0) == -1) {
        JSONObject obj = new JSONObject();
        obj.put("response", builder.toString()); // .append("response", );
        logger.debug("data: " + builder.toString());
        json = obj;

      } else {
        json = new JSONObject(builder.toString());
      }
    } catch (Exception e) {
      logger.error("", e);
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
    return json;

  }

  public static JSONArray convertToJSONArray(HttpResponse response) throws IOException {
    JSONArray json = new JSONArray();
    BufferedReader reader = null;
    HttpEntity entity = response.getEntity();
    try {
      reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
      StringBuilder builder = new StringBuilder();
      for (String line = null; (line = reader.readLine()) != null;) {
        builder.append(line).append("\n");
      }
      if (builder.indexOf("{", 0) == -1) {
        JSONArray obj = new JSONArray();
        obj.put((new HashMap<String, String>()).put("response", builder.toString())); // .append("response",
                                                                                      // );
        logger.debug("data: " + builder.toString());
        json = obj;

      } else {
        json = new JSONArray(builder.toString());
      }
    } catch (Exception e) {
      logger.error("", e);
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
    return json;

  }

  public static String convertEntityToString(HttpEntity entity) throws IOException {
    String r = null;
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
      StringBuilder builder = new StringBuilder();
      for (String line = null; (line = reader.readLine()) != null;) {
        builder.append(line).append("\n");
      }
      r = builder.toString();
    } finally {
      reader.close();
    }
    return r;

  }

  public static JSONObject convertStringToJSON(String string) throws IOException {
    JSONObject json = new JSONObject();
    if (string == null) {
      return json;
    }
    try {
      StringBuilder builder = new StringBuilder(string);
      if (builder.indexOf("{", 0) == -1) {
        JSONObject obj = new JSONObject();
        obj.put("response", builder.toString()); // .append("response", );
        logger.debug("data: " + builder.toString());
        json = obj;

      } else {
        json = new JSONObject(builder.toString());
      }
    } catch (Exception e) {
      logger.error("", e);
    } finally {

    }
    return json;
  }

  public static String getDaPaasEndpoint(String apiName) {
    String address = Config.getInstance().getDaPaasServer() + apiName;
    return address;
  }

  public static boolean checkCatalog(String apiKey, String apiSecret) {
    List<Dataset> catalog = new ArrayList<Dataset>();
    DatasetCatalogHandler handler = new DatasetCatalogHandler(apiKey, apiSecret);
    catalog = handler.getDatasetCatalog();
    System.out.println("Check catalogs count "+catalog.size()+"apiKey: "+apiKey+"   apiSecret: " + apiSecret);
    return catalog.size() > 0;
  }

  public static HashMap<String, String> convertColumn(String responseStr) {
    // "head" : {
    // "vars" : [ "title", "Seoul", "Busan", "Daegu", "Incheon", "Gwangju",
    // "Daejeon", "Ulsan", "Gyeonggido", "Gangwondo", "Chungcheongbukdo",
    // "Chungcheongnamdo", "Jeollabukdo", "Jeollanamdo", "Gyeongsangbukdo",
    // "Gyeongsangnamdo" ]
    // },
    if (responseStr == null) {
      return new HashMap<String, String>();
    }
    String q = responseStr.replaceAll("\r\n", " ");
    q = q.replaceAll("\n", " ");
    q = q.substring(q.indexOf("\"vars\" : ["));
    q = q.substring(0, q.indexOf("]"));
    q = q.substring(q.indexOf("[") + 1);
    q = q.replaceAll(",", "");
    q = q.replaceAll("\"", "");
    int i = 0;
    HashMap<String, String> columns = new HashMap<String, String>();
    String[] qq = q.trim().split(" ");
    while (i < qq.length) {
      String col = qq[i];
      columns.put("column_" + i, col);
      i++;
    }
    return columns;
  }

  public static String htmlEncoding(String text) {
    try {
      return URLEncoder.encode(text, "UTF-8").replace("%2B", "+");
    } catch (Throwable t) {
      t.printStackTrace();
      return text;
    }
  }

  public static String escapeJS(String value) {
    return StringEscapeUtils.escapeJavaScript(value);
  }
}
