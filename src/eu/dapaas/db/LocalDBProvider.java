package eu.dapaas.db;

import java.util.ArrayList;
import java.util.List;

import eu.dapaas.dao.Poligon;
import eu.dapaas.dao.PortalContent;
import eu.dapaas.dao.WizardPortal;
import eu.dapaas.db.dynamo.DynamoProvider;
import eu.dapaas.db.sqlite.SQLiteProvider;
import eu.dapaas.utils.Config;


public class LocalDBProvider {

  public static void createDB(){
    if (Config.getInstance().getDapaasLocalDB().equals("dynamo")) {
      DynamoProvider.createDB();
    }
    if (Config.getInstance().getDapaasLocalDB().equals("sqlite")) {
      SQLiteProvider.createDB();
    }
  }

  
  public static void updateDB(){
    if (Config.getInstance().getDapaasLocalDB().equals("dynamo")) {
      DynamoProvider.updateDB();
    }
    if (Config.getInstance().getDapaasLocalDB().equals("sqlite")) {
      SQLiteProvider.updateDB();
    }
  }

  
  public static void destroyDB(){
    if (Config.getInstance().getDapaasLocalDB().equals("dynamo")) {
      DynamoProvider.destroyDB();
    }
    if (Config.getInstance().getDapaasLocalDB().equals("sqlite")) {
      SQLiteProvider.destroyDB();
    }
  }
  
  public static void unloadAllNativeLibs() {
    if (Config.getInstance().getDapaasLocalDB().equals("dynamo")) {
      DynamoProvider.unloadAllNativeLibs();
    }
    if (Config.getInstance().getDapaasLocalDB().equals("sqlite")) {
      SQLiteProvider.unloadAllNativeLibs();
    }
  }


  public static void insertDSPortal(WizardPortal portal){
    if (Config.getInstance().getDapaasLocalDB().equals("dynamo")) {
      DynamoProvider.insertDSPortal(portal);
    }
    if (Config.getInstance().getDapaasLocalDB().equals("sqlite")) {
      SQLiteProvider.insertDSPortal(portal);
    }
  }

  public static void deleteDSPortal(String datasetId){
    if (Config.getInstance().getDapaasLocalDB().equals("dynamo")) {
      DynamoProvider.deleteDSPortal(datasetId);
    }
    if (Config.getInstance().getDapaasLocalDB().equals("sqlite")) {
      SQLiteProvider.deleteDSPortal(datasetId);
    }
  }

  public static WizardPortal getPortaltByParam(String param){
    WizardPortal result = null;
    if (Config.getInstance().getDapaasLocalDB().equals("dynamo")) {
      result = DynamoProvider.getPortaltByParam(param);
    }
    if (Config.getInstance().getDapaasLocalDB().equals("sqlite")) {
      result = SQLiteProvider.getPortaltByParam(param);
    }
    return result;
  }

  public static WizardPortal getPortaltByDatasetId(String datasetId){
    WizardPortal result = null;
    if (Config.getInstance().getDapaasLocalDB().equals("dynamo")) {
      result = DynamoProvider.getPortaltByDatasetId(datasetId);
    }
    if (Config.getInstance().getDapaasLocalDB().equals("sqlite")) {
      result = SQLiteProvider.getPortaltByDatasetId(datasetId);
    }
    return result;
  }

  public static PortalContent getPortalContent(String portalContentId){
    PortalContent result = null;
    if (Config.getInstance().getDapaasLocalDB().equals("dynamo")) {
      result = DynamoProvider.getPortalContent(portalContentId);
    }
    if (Config.getInstance().getDapaasLocalDB().equals("sqlite")) {
      result = SQLiteProvider.getPortalContent(portalContentId);
    }
    return result;
  }

  public static boolean checkPortalParameter(String portalParameter, String datasetId){
    boolean result = false;
    if (Config.getInstance().getDapaasLocalDB().equals("dynamo")) {
      result = DynamoProvider.checkPortalParameter(portalParameter, datasetId);
    }
    if (Config.getInstance().getDapaasLocalDB().equals("sqlite")) {
      result = SQLiteProvider.checkPortalParameter(portalParameter, datasetId);
    }
    return result;
  }
  
  public static List<Poligon> getPoligons(){
    List<Poligon> result = new ArrayList<Poligon>();
    if (Config.getInstance().getDapaasLocalDB().equals("dynamo")) {
      result = DynamoProvider.getPoligons();
    }
    if (Config.getInstance().getDapaasLocalDB().equals("sqlite")) {
      result = SQLiteProvider.getPoligons();
    }
    return result;
  }
  
  public static void createPoligon(Poligon poligon){
    if (Config.getInstance().getDapaasLocalDB().equals("dynamo")) {
       DynamoProvider.createPoligon(poligon);
    }
    if (Config.getInstance().getDapaasLocalDB().equals("sqlite")) {
       SQLiteProvider.createPoligon(poligon);
    }
  }
  public static void updatePoligon(Poligon poligon){
    if (Config.getInstance().getDapaasLocalDB().equals("dynamo")) {
       DynamoProvider.updatePoligon(poligon);
    }
    if (Config.getInstance().getDapaasLocalDB().equals("sqlite")) {
       SQLiteProvider.updatePoligon(poligon);
    }
  }
  
  public static void deletePoligon(String poligonId){
    if (Config.getInstance().getDapaasLocalDB().equals("dynamo")) {
       DynamoProvider.deletePoligon(poligonId);
    }
    if (Config.getInstance().getDapaasLocalDB().equals("sqlite")) {
       SQLiteProvider.deletePoligon(poligonId);
    }
  }
  
  public static Poligon getPoligon(String poligonId){
    Poligon result = null;
    if (Config.getInstance().getDapaasLocalDB().equals("dynamo")) {
      result = DynamoProvider.getPoligon(poligonId);
    }
    if (Config.getInstance().getDapaasLocalDB().equals("sqlite")) {
      result = SQLiteProvider.getPoligon(poligonId);
    }
    return result;
  }
}
