package eu.dapaas.db.sqlite;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.sqlite.SQLiteJDBCLoader;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import eu.dapaas.dao.Poligon;
import eu.dapaas.dao.PortalContent;
import eu.dapaas.dao.WizardPortal;
import eu.dapaas.db.LocalDBProvider;
import eu.dapaas.db.dynamo.DynamoDSPoligon;

public class SQLiteProvider extends LocalDBProvider {
  private static final SQLiteProvider instance = new SQLiteProvider();
  private static final Logger         logger   = Logger.getLogger(SQLiteProvider.class);

  public static SQLiteProvider getInstance() {
    return instance;
  }

  private SQLiteProvider() {
  }

  public static void createDB() {
    Connection connection = null;
    try {
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:dapaas.db");

      Runtime.getRuntime().addShutdownHook(new Thread() {
        public void run() {
          unloadAllNativeLibs();
        }
      });

      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30); // set timeout to 30 sec.
      statement = connection.createStatement();
      String sql = "CREATE TABLE DS_PORTAL " +
          "(" +
          " DS_ID TEXT NOT NULL," +
          " PARAM CHAR(100) NOT NULL," +
          " TITLE TEXT NOT NULL" +
          " )";
      statement.executeUpdate(sql);
      sql = "CREATE TABLE DS_CONTENT " +
          "(" +
          " ID    INTEGER PRIMARY KEY, " +
          " DS_ID TEXT NOT NULL, " +
          " CHART CHAR(100) NOT NULL, " +
          " QUERY TEXT NOT NULL, " +
          " TITLE TEXT NOT NULL, " +
          " DESCRIPTION TEXT NULL, " +
          " SUMMARY TEXT NULL, " +
          " DATE_PATTERN CHAR(100) NULL, " +
          " ACCESS_URL TEXT NOT NULL, " +
          " POLIGON_ID TEXT NULL" +
          " )";
      statement.executeUpdate(sql);

      sql = "CREATE TABLE DS_POLIGON " +
          "(" +
          " ID    INTEGER PRIMARY KEY, " +
          " TITLE TEXT NOT NULL, " +
          " FILENAME TEXT NOT NULL " +
          " )";
      statement.executeUpdate(sql);
      statement.close();
    } catch (SQLException e) {
      logger.error("", e);
    } catch (Throwable e) {
      logger.error("", e);
      ;
    } finally {

      try {
        if (connection != null)
          connection.close();
      } catch (SQLException e) {
        System.out.println(e);
      }
    }
  }

  public static void updateDB() {
    Connection connection = null;
    try {
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:dapaas.db");

      Runtime.getRuntime().addShutdownHook(new Thread() {
        public void run() {
          unloadAllNativeLibs();
        }
      });

      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30); // set timeout to 30 sec.
      statement = connection.createStatement();
      // String sql = "ALTER TABLE DS_CONTENT ADD DESCRIPTION TEXT NULL";
      // statement.executeUpdate(sql);
      // sql = "ALTER TABLE DS_CONTENT ADD SUMMARY TEXT NULL";
      // statement.executeUpdate(sql);
      String sql = "ALTER TABLE DS_PORTAL ADD TITLE TEXT NULL";
      statement.executeUpdate(sql);

      statement.close();
    } catch (SQLException e) {
      logger.error("", e);
    } catch (Throwable e) {
      logger.error("", e);
      ;
    } finally {

      try {
        if (connection != null)
          connection.close();
      } catch (SQLException e) {
        System.out.println(e);
      }
    }
  }

  public static void destroyDB() {
    Enumeration<Driver> drivers = DriverManager.getDrivers();
    while (drivers.hasMoreElements()) {
      Driver driver = drivers.nextElement();
      try {
        DriverManager.deregisterDriver(driver);
        logger.info(String.format("deregistering jdbc driver: %s", driver));
        driver = null;
      } catch (SQLException e) {
        logger.error(String.format("Error deregistering driver %s", driver), e);
      }
    }
    unloadAllNativeLibs();
    System.runFinalization();
    System.gc();
  }

  public static void unloadAllNativeLibs() {
    try {
      ClassLoader classLoader = SQLiteJDBCLoader.class.getClassLoader();
      Field field = ClassLoader.class.getDeclaredField("nativeLibraries");
      field.setAccessible(true);
      Vector<Object> libs = (Vector<Object>) field.get(classLoader);
      Iterator it = libs.iterator();
      while (it.hasNext()) {
        Object object = it.next();
        Method finalize = object.getClass().getDeclaredMethod("finalize");
        finalize.setAccessible(true);
        finalize.invoke(object);
      }
    } catch (Throwable th) {
      th.printStackTrace();
    }
  }

  public static void insertDSPortal(WizardPortal portal) {
    Connection connection = null;
    try {
      String insertDS = "INSERT INTO DS_PORTAL " +
          "(" +
          " DS_ID," +
          " PARAM," +
          " TITLE " +
          " ) VALUES(?, ?, ?)";

      String insertDSC = "INSERT INTO DS_CONTENT " +
          "(" +
          " DS_ID," +
          " CHART, " +
          " QUERY, " +
          " TITLE, " +
          " DESCRIPTION, " +
          " SUMMARY, " +
          " DATE_PATTERN, " +
          " ACCESS_URL," +
          " POLIGON_ID" +
          " ) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:dapaas.db");
      PreparedStatement st = connection.prepareStatement(insertDS);
      st.setString(1, portal.getDatasetId());
      st.setString(2, portal.getParameter());
      st.setString(3, portal.getTitle());
      st.executeUpdate();
      st.close();

      for (PortalContent pc : portal.getPortalContent()) {
        PreparedStatement std = connection.prepareStatement(insertDSC);
        std.setString(1, portal.getDatasetId());
        std.setString(2, pc.getChart());
        std.setString(3, pc.getQuery());
        std.setString(4, pc.getTitle());
        std.setString(5, pc.getDescription());
        std.setString(6, pc.getSummary());
        std.setString(7, pc.getDatePattern());
        std.setString(8, pc.getAccessURL());
        std.setString(9, pc.getPoligon().getId());
        std.executeUpdate();
        std.close();
      }
    } catch (SQLException e) {
      logger.error("", e);
      ;
    } catch (Exception e) {
      logger.error("", e);
      ;
    } finally {

      try {
        if (connection != null)
          connection.close();
      } catch (SQLException e) {
        System.err.println(e);
      }
    }
  }

  public static void deleteDSPortal(String datasetId) {
    Connection connection = null;
    try {
      String deleteDS = "DELETE FROM DS_PORTAL " +
          " WHERE " +
          " DS_ID = ?";

      String deleteDSC = "DELETE FROM DS_CONTENT " +
          " WHERE " +
          " DS_ID = ?";
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:dapaas.db");
      PreparedStatement ps = connection.prepareStatement(deleteDS);
      ps.setString(1, datasetId);

      ps.executeUpdate();
      ps.close();

      PreparedStatement ps1 = connection.prepareStatement(deleteDSC);
      ps1.setString(1, datasetId);
      ps1.executeUpdate();
      ps1.close();

    } catch (SQLException e) {
      logger.error("", e);
      ;
    } catch (Exception e) {
      logger.error("", e);
      ;
    } finally {

      try {
        if (connection != null)
          connection.close();
      } catch (SQLException e) {
        System.err.println(e);
      }
    }

  }

  public static WizardPortal getPortaltByParam(String param) {
    Connection connection = null;
    WizardPortal portal = new WizardPortal();
    try {
      String selectDS = "SELECT DS_ID, TITLE FROM DS_PORTAL " +
          " WHERE " +
          " PARAM = ?";
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:dapaas.db");
      PreparedStatement ps = connection.prepareStatement(selectDS);
      ps.setString(1, param);

      // process the results
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        portal.setDatasetId(rs.getString("DS_ID"));
        portal.setParameter(param);
        portal.setTitle(rs.getString("TITLE"));
      }
      rs.close();
      ps.close();

      String selectDSC = "SELECT ID, CHART, QUERY, TITLE, DESCRIPTION, SUMMARY, DATE_PATTERN, ACCESS_URL, POLIGON_ID FROM DS_CONTENT " +
          " WHERE " +
          " DS_ID = ?";
      PreparedStatement ps1 = connection.prepareStatement(selectDSC);
      ps1.setString(1, portal.getDatasetId());

      // process the results
      ResultSet rs1 = ps1.executeQuery();
      while (rs1.next()) {
        PortalContent pc = new PortalContent();
        pc.setChart(rs1.getString("CHART"));
        pc.setQuery(rs1.getString("QUERY"));
        pc.setId(rs1.getString("ID"));
        pc.setTitle(rs1.getString("TITLE"));
        pc.setDatePattern(rs1.getString("DATE_PATTERN"));
        pc.setAccessURL(rs1.getString("ACCESS_URL"));
        pc.setDescription(rs1.getString("DESCRIPTION"));
        pc.setSummary(rs1.getString("SUMMARY"));
        String poligonId = rs1.getString("POLIGON_ID");
        pc.setSummaryHtml(pc.getSummaryHtml());
        pc.setDescriptionHtml(pc.getDescriptionHtml());
        pc.setDatasetId(portal.getDatasetId());
        Poligon poligon = getPoligon(poligonId);
        if (poligon != null) {
          pc.setPoligon(poligon);
        }
        portal.getPortalContent().add(pc);
      }
      rs1.close();
      ps1.close();

    } catch (SQLException e) {
      logger.error("", e);
      ;
    } catch (Exception e) {
      logger.error("", e);
      ;
    } finally {

      try {
        if (connection != null)
          connection.close();
      } catch (SQLException e) {
        System.err.println(e);
      }
    }
    return portal;
  }

  public static WizardPortal getPortaltByDatasetId(String datasetId) {
    Connection connection = null;
    WizardPortal portal = new WizardPortal();
    try {
      String selectDS = "SELECT PARAM, TITLE FROM DS_PORTAL " +
          " WHERE " +
          " DS_ID = ?";
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:dapaas.db");
      PreparedStatement ps = connection.prepareStatement(selectDS);
      ps.setString(1, datasetId);

      // process the results
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        portal.setDatasetId(datasetId);
        portal.setParameter(rs.getString("PARAM"));
        portal.setTitle(rs.getString("TITLE"));
      }
      rs.close();
      ps.close();

      String selectDSC = "SELECT ID, CHART, QUERY, TITLE, DESCRIPTION, SUMMARY, DATE_PATTERN, ACCESS_URL, POLIGON_ID FROM DS_CONTENT " +
          " WHERE " +
          " DS_ID = ? ORDER BY ID";
      PreparedStatement ps1 = connection.prepareStatement(selectDSC);
      ps1.setString(1, portal.getDatasetId());

      // process the results
      ResultSet rs1 = ps1.executeQuery();
      while (rs1.next()) {
        PortalContent pc = new PortalContent();
        pc.setChart(rs1.getString("CHART"));
        pc.setQuery(rs1.getString("QUERY"));
        pc.setId(rs1.getString("ID"));
        pc.setTitle(rs1.getString("TITLE"));
        pc.setDatePattern(rs1.getString("DATE_PATTERN"));
        pc.setAccessURL(rs1.getString("ACCESS_URL"));
        pc.setDescription(rs1.getString("DESCRIPTION"));
        pc.setSummary(rs1.getString("SUMMARY"));
        // pc.setPoligonId(rs1.getString("POLIGON_ID"));
        String poligonId = rs1.getString("POLIGON_ID");
        pc.setSummaryHtml(pc.getSummaryHtml());
        pc.setDescriptionHtml(pc.getDescriptionHtml());
        pc.setDatasetId(portal.getDatasetId());
        Poligon poligon = getPoligon(poligonId);
        if (poligon != null) {
          pc.setPoligon(poligon);
        }
        portal.getPortalContent().add(pc);
      }
      rs1.close();
      ps1.close();

    } catch (SQLException e) {
      logger.error("", e);
      ;
    } catch (Exception e) {
      logger.error("", e);
      ;
    } finally {

      try {
        if (connection != null)
          connection.close();
      } catch (SQLException e) {
        System.err.println(e);
      }
    }
    return portal;
  }

  public static PortalContent getPortalContent(String portalContentId) {
    Connection connection = null;
    PortalContent portalContent = new PortalContent();
    Poligon poligon = null;
    String poligonId = null;
    try {

      String selectDSC = "SELECT ID, CHART, QUERY, TITLE, DESCRIPTION, SUMMARY, DS_ID, DATE_PATTERN, ACCESS_URL, POLIGON_ID FROM DS_CONTENT " +
          " WHERE " +
          " ID = ?";
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:dapaas.db");
      PreparedStatement ps1 = connection.prepareStatement(selectDSC);
      ps1.setString(1, portalContentId);
      // process the results
      ResultSet rs1 = ps1.executeQuery();
      while (rs1.next()) {
        portalContent.setChart(rs1.getString("CHART"));
        portalContent.setQuery(rs1.getString("QUERY"));
        portalContent.setId(portalContentId);
        portalContent.setTitle(rs1.getString("TITLE"));
        portalContent.setDatasetId(rs1.getString("DS_ID"));
        portalContent.setDatePattern(rs1.getString("DATE_PATTERN"));
        portalContent.setAccessURL(rs1.getString("ACCESS_URL"));
        portalContent.setDescription(rs1.getString("DESCRIPTION"));
        portalContent.setSummary(rs1.getString("SUMMARY"));
        // portalContent.setPoligonId();
        poligonId = rs1.getString("POLIGON_ID");
        portalContent.setSummaryHtml(portalContent.getSummaryHtml());
        portalContent.setDescriptionHtml(portalContent.getDescriptionHtml());
      }
      rs1.close();
      ps1.close();

      poligon = getPoligon(poligonId);
      if (poligon != null) {
        portalContent.setPoligon(poligon);
      }
    } catch (SQLException e) {
      logger.error("", e);
      ;
    } catch (Exception e) {
      logger.error("", e);
    } finally {

      try {
        if (connection != null)
          connection.close();
      } catch (SQLException e) {
        System.err.println(e);
      }
    }
    return portalContent;
  }

  public static boolean checkPortalParameter(String portalParameter, String datasetId) {
    if (datasetId != null) {
      WizardPortal portal = getPortaltByParam(portalParameter);
      if (portal != null && portal.getParameter() != null && !portal.getDatasetId().equals(datasetId)) {
        return true;
      }
      return false;
    } else {
      WizardPortal portal = getPortaltByParam(portalParameter);
      if (portal != null && portal.getParameter() != null) {
        return true;
      }
      return false;
    }
  }

  public static List<Poligon> getPoligons() {
    List<Poligon> result = new ArrayList<Poligon>();
    Connection connection = null;
    try {

      String selectDSC = "SELECT ID, TITLE, FILENAME FROM DS_POLIGON ";
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:dapaas.db");
      PreparedStatement ps1 = connection.prepareStatement(selectDSC);
      // process the results
      ResultSet rs1 = ps1.executeQuery();
      while (rs1.next()) {
        Poligon poligon = new Poligon();
        poligon.setId(rs1.getString("ID"));
        poligon.setTitle(rs1.getString("TITLE"));
        poligon.setFilename(rs1.getString("FILENAME"));
        result.add(poligon);
      }
      rs1.close();
      ps1.close();

    } catch (SQLException e) {
      logger.error("", e);
      ;
    } catch (Exception e) {
      logger.error("", e);
    } finally {

      try {
        if (connection != null)
          connection.close();
      } catch (SQLException e) {
        System.err.println(e);
      }
    }
    return result;
  }

  public static void createPoligon(Poligon poligon) {
    Connection connection = null;
    try {
      String insertDS = "INSERT INTO DS_POLIGON " +
          "(" +
          " " +
          " TITLE," +
          " FILENAME " +
          " ) VALUES( ?, ?)";

      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:dapaas.db");
      PreparedStatement st = connection.prepareStatement(insertDS);
      st.setString(1, poligon.getTitle());
      st.setString(2, poligon.getFilename());
      st.executeUpdate();
      st.close();
    } catch (SQLException e) {
      logger.error("", e);
      ;
    } catch (Exception e) {
      logger.error("", e);
      ;
    } finally {

      try {
        if (connection != null)
          connection.close();
      } catch (SQLException e) {
        System.err.println(e);
      }
    }
  }

  public static Poligon getPoligon(String poligonId) {
    Poligon result = null;
    Connection connection = null;
    try {

      String selectDSC = "SELECT ID, TITLE, FILENAME FROM DS_POLIGON WHERE id=?";
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:dapaas.db");
      PreparedStatement ps1 = connection.prepareStatement(selectDSC);
      ps1.setString(1, poligonId);
      // process the results
      ResultSet rs1 = ps1.executeQuery();
      while (rs1.next()) {
        result = new Poligon();
        result.setId(rs1.getString("ID"));
        result.setTitle(rs1.getString("TITLE"));
        result.setFilename(rs1.getString("FILENAME"));
      }
      rs1.close();
      ps1.close();

    } catch (SQLException e) {
      logger.error("", e);
      ;
    } catch (Exception e) {
      logger.error("", e);
    } finally {

      try {
        if (connection != null)
          connection.close();
      } catch (SQLException e) {
        System.err.println(e);
      }
    }
    return result;
  }

  public static void deletePoligon(String poligonId) {
    Connection connection = null;
    try {
      String deleteDS = "DELETE FROM DS_POLIGON " +
          " WHERE " +
          " ID = ?";

      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:dapaas.db");
      PreparedStatement ps = connection.prepareStatement(deleteDS);
      ps.setString(1, poligonId);

      ps.executeUpdate();
      ps.close();

    } catch (SQLException e) {
      logger.error("", e);
      ;
    } catch (Exception e) {
      logger.error("", e);
      ;
    } finally {

      try {
        if (connection != null)
          connection.close();
      } catch (SQLException e) {
        System.err.println(e);
      }
    }
  }
  
  public static void updatePoligon(Poligon poligon) {
    Poligon result = null;
    Connection connection = null;
    try {
      String updateDSC = "UPDATE DS_POLIGON SET TITLE=?, FILENAME=? WHERE id=?";
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:dapaas.db");
      PreparedStatement st = connection.prepareStatement(updateDSC);
      st.setString(1, poligon.getTitle());
      st.setString(2, poligon.getFilename());
      st.setString(3, poligon.getId());
      st.executeUpdate();
      st.close();

    } catch (SQLException e) {
      logger.error("", e);
      ;
    } catch (Exception e) {
      logger.error("", e);
    } finally {

      try {
        if (connection != null)
          connection.close();
      } catch (SQLException e) {
        System.err.println(e);
      }
    }
  }
}
