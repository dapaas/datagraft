package eu.dapaas.db.dynamo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.Tables;

import eu.dapaas.dao.Poligon;
import eu.dapaas.dao.PortalContent;
import eu.dapaas.dao.WizardPortal;

public class DynamoProvider {

  private static final Logger         logger = Logger.getLogger(DynamoProvider.class);
  private static AmazonDynamoDBClient dynamoDBClient;

  private static void init() {
    dynamoDBClient = new AmazonDynamoDBClient(); //new InstanceProfileCredentialsProvider()
    Region usWest1 = Region.getRegion(Regions.EU_WEST_1);
    dynamoDBClient.setRegion(usWest1);
  }

  public static void createDB() {
    init();
    DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
    try {
      if (Tables.doesTableExist(dynamoDBClient, "DS_PORTAL")) {
        System.out.println("Table " + "DS_PORTAL" + " is already ACTIVE");
//        Table table = dynamoDB.getTable("DS_PORTAL");
//        table.delete();
//        table.waitForDelete();
      } else {
        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("DS_ID").withAttributeType("S"));

        ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
        keySchema.add(new KeySchemaElement().withAttributeName("DS_ID").withKeyType(KeyType.HASH));
        CreateTableRequest createTablePortalRequest = new CreateTableRequest().withTableName("DS_PORTAL")
            .withKeySchema(keySchema)
            .withAttributeDefinitions(attributeDefinitions)
            .withProvisionedThroughput(new ProvisionedThroughput()
                .withReadCapacityUnits(5L)
                .withWriteCapacityUnits(6L));

        TableDescription createdTablePortalDescription = dynamoDB.createTable(createTablePortalRequest).getDescription();
        logger.info("Created Table DS_PORTAL: " + createdTablePortalDescription);
      }

      if (Tables.doesTableExist(dynamoDBClient, "DS_CONTENT")) {
        System.out.println("Table " + "DS_CONTENT" + " is already ACTIVE");
//        Table table = dynamoDB.getTable("DS_CONTENT");
//        table.delete();
//        table.waitForDelete();
      } else {
        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("ID").withAttributeType("S"));

        ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
        keySchema.add(new KeySchemaElement().withAttributeName("ID").withKeyType(KeyType.HASH));
        CreateTableRequest createTableContentRequest = new CreateTableRequest().withTableName("DS_CONTENT")
            .withKeySchema(keySchema)
            .withAttributeDefinitions(attributeDefinitions)
            .withProvisionedThroughput(new ProvisionedThroughput()
                .withReadCapacityUnits(5L)
                .withWriteCapacityUnits(6L));

        TableDescription createdTableContentDescription = dynamoDB.createTable(createTableContentRequest).getDescription();
        logger.info("Created Table DS_CONTENT: " + createdTableContentDescription);
      }
      
      if (Tables.doesTableExist(dynamoDBClient, "DS_POLIGON")) {
        System.out.println("Table " + "DS_POLIGON" + " is already ACTIVE");
//        Table table = dynamoDB.getTable("DS_CONTENT");
//        table.delete();
//        table.waitForDelete();
      } else {
        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("ID").withAttributeType("S"));

        ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
        keySchema.add(new KeySchemaElement().withAttributeName("ID").withKeyType(KeyType.HASH));
        CreateTableRequest createTableContentRequest = new CreateTableRequest().withTableName("DS_POLIGON")
            .withKeySchema(keySchema)
            .withAttributeDefinitions(attributeDefinitions)
            .withProvisionedThroughput(new ProvisionedThroughput()
                .withReadCapacityUnits(5L)
                .withWriteCapacityUnits(6L));

        TableDescription createdTableContentDescription = dynamoDB.createTable(createTableContentRequest).getDescription();
        logger.info("Created Table DS_POLIGON: " + createdTableContentDescription);
      }
    } catch (Throwable e) {
      logger.error("", e);
    } finally {

    }
  }
  public static void updateDB() {
    init();
  }
  
  public static void deleteDB() {
    init();
    try {
      DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
      if (Tables.doesTableExist(dynamoDBClient, "DS_PORTAL")) {
        Table table = dynamoDB.getTable("DS_PORTAL");
        table.delete();
        System.out.println("Table " + "DS_PORTAL" + " is already ACTIVE Delete !");
        table.waitForDelete();
      }
      if (Tables.doesTableExist(dynamoDBClient, "DS_CONTENT")) {
        Table table = dynamoDB.getTable("DS_CONTENT");
        table.delete();
        System.out.println("Table " + "DS_PORTAL" + " is already ACTIVE Delete !");
        table.waitForDelete();
      }
    }catch(Throwable t){
      logger.error("", t);
    }
    System.gc();
  }
  
  public static void destroyDB() {
    System.gc();
  }

  public static void unloadAllNativeLibs() {

  }

  private static DynamoDSPortal convertPortal(WizardPortal portal){
    DynamoDSPortal item = new DynamoDSPortal();
    item.setDsId(portal.getDatasetId());
    item.setParam(portal.getParameter());
    item.setTitle(portal.getTitle());
    return item;
  }
  
  private static DynamoDSContent convertContent(PortalContent pc, String datasetId){
      DynamoDSContent item = new DynamoDSContent();
      item.setDsId(datasetId);
      item.setChart(pc.getChart());
      item.setQuery(pc.getQuery());
      item.setTitle(pc.getTitle());
      item.setDescription(pc.getDescription());
      item.setSummary(pc.getSummary());
      item.setDatePattern(pc.getDatePattern());
      item.setAccessURL(pc.getAccessURL());
      if (pc.getPoligon() != null){
        item.setPoligonId(pc.getPoligon().getId());
      }
      return item;
  }
  
  private static DynamoDSPoligon convertPoligon(Poligon poligon){
    DynamoDSPoligon item = new DynamoDSPoligon();
    item.setFilename(poligon.getFilename());
    item.setTitle(poligon.getTitle());
    item.setId(poligon.getId());
    return item;
  }
  
  public static void insertDSPortal(WizardPortal portal) {
    try {
      DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
      mapper.save(convertPortal(portal));
      
      for (PortalContent pc : portal.getPortalContent()) {
        mapper.save(convertContent(pc, portal.getDatasetId()));
      }
    } catch (Throwable e) {
      logger.error("", e);
    } 
  }

  public static void deleteDSPortal(String datasetId) {
    
    try {
      
      DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
      DynamoDBScanExpression scanPortalExpression = new DynamoDBScanExpression();
      Map<String, Condition> scanPortalFilter = new HashMap<String, Condition>();
      scanPortalFilter.put("DS_ID", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue().withS(datasetId)));
      scanPortalExpression.setScanFilter(scanPortalFilter);

      List<DynamoDSPortal> portals = mapper.scan(DynamoDSPortal.class, scanPortalExpression);
      mapper.batchDelete(portals);
     
      
      DynamoDBScanExpression scanContentExpression = new DynamoDBScanExpression();
      Map<String, Condition> scanContentFilter = new HashMap<String, Condition>();
      scanContentFilter.put("DS_ID", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue().withS(datasetId)));
      scanContentExpression.setScanFilter(scanContentFilter);

      List<DynamoDSContent> contents = mapper.scan(DynamoDSContent.class, scanContentExpression);
      mapper.batchDelete(contents);
      
    } catch (Throwable e) {
      logger.error("", e);
    }
  }

  public static WizardPortal getPortaltByParam(String param) {
    WizardPortal portal = new WizardPortal();
    try {
      DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
      DynamoDBScanExpression scanPortalExpression = new DynamoDBScanExpression();
      Map<String, Condition> scanPortalFilter = new HashMap<String, Condition>();
      scanPortalFilter.put("PARAM", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue().withS(param)));
      scanPortalExpression.setScanFilter(scanPortalFilter);

      List<DynamoDSPortal> portals = mapper.scan(DynamoDSPortal.class, scanPortalExpression);
      for (DynamoDSPortal dp : portals){
        portal.setDatasetId(dp.getDsId());
        portal.setParameter(dp.getParam());
        portal.setTitle(dp.getTitle());
      }
      
      DynamoDBScanExpression scanContentExpression = new DynamoDBScanExpression();
      Map<String, Condition> scanContentFilter = new HashMap<String, Condition>();
      scanContentFilter.put("DS_ID", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue().withS(portal.getDatasetId())));
      scanContentExpression.setScanFilter(scanContentFilter);

      List<DynamoDSContent> contents = mapper.scan(DynamoDSContent.class, scanContentExpression);
      for (DynamoDSContent dc : contents){
        PortalContent pc = new PortalContent();
        pc.setChart(dc.getChart());
        pc.setQuery(dc.getQuery());
        pc.setId(dc.getId());
        pc.setTitle(dc.getTitle());
        pc.setDatePattern(dc.getDatePattern());
        pc.setAccessURL(dc.getAccessURL());
        pc.setDescription(dc.getDescription());
        pc.setSummary(dc.getSummary());
        pc.setSummaryHtml(pc.getSummaryHtml());
        pc.setDescriptionHtml(pc.getDescriptionHtml());
        pc.setDatasetId(portal.getDatasetId());
        if (dc.getPoligonId() != null){
          pc.setPoligon(getPoligon(dc.getPoligonId()));
        }
        portal.getPortalContent().add(pc);
      }    
    } catch (Throwable e) {
      logger.error("", e);
    }
    return portal;
  }

  public static WizardPortal getPortaltByDatasetId(String datasetId) {
    
    WizardPortal portal = new WizardPortal();
    try {
      DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
      DynamoDBScanExpression scanPortalExpression = new DynamoDBScanExpression();
      Map<String, Condition> scanPortalFilter = new HashMap<String, Condition>();
      scanPortalFilter.put("DS_ID", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue().withS(datasetId)));
      scanPortalExpression.setScanFilter(scanPortalFilter);

      List<DynamoDSPortal> portals = mapper.scan(DynamoDSPortal.class, scanPortalExpression);
      for (DynamoDSPortal dp : portals){
        portal.setDatasetId(dp.getDsId());
        portal.setParameter(dp.getParam());
        portal.setTitle(dp.getTitle());
      }
      
      DynamoDBScanExpression scanContentExpression = new DynamoDBScanExpression();
      Map<String, Condition> scanContentFilter = new HashMap<String, Condition>();
      scanContentFilter.put("DS_ID", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue().withS(portal.getDatasetId())));
      scanContentExpression.setScanFilter(scanContentFilter);

      List<DynamoDSContent> contents = mapper.scan(DynamoDSContent.class, scanContentExpression);
      for (DynamoDSContent dc : contents){
        PortalContent pc = new PortalContent();
        pc.setChart(dc.getChart());
        pc.setQuery(dc.getQuery());
        pc.setId(dc.getId());
        pc.setTitle(dc.getTitle());
        pc.setDatePattern(dc.getDatePattern());
        pc.setAccessURL(dc.getAccessURL());
        pc.setDescription(dc.getDescription());
        pc.setSummary(dc.getSummary());
        pc.setSummaryHtml(pc.getSummaryHtml());
        pc.setDescriptionHtml(pc.getDescriptionHtml());
        pc.setDatasetId(portal.getDatasetId());
        if (dc.getPoligonId() != null){
          pc.setPoligon(getPoligon(dc.getPoligonId()));
        }
        portal.getPortalContent().add(pc);
      } 
    } catch (Throwable e) {
      logger.error("", e);
    }
    return portal;
  }

  public static PortalContent getPortalContent(String portalContentId) {
    PortalContent portalContent = new PortalContent();
    try {
      DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
      DynamoDBScanExpression scanContentExpression = new DynamoDBScanExpression();
      Map<String, Condition> scanContentFilter = new HashMap<String, Condition>();
      scanContentFilter.put("ID", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue().withS(portalContentId)));
      scanContentExpression.setScanFilter(scanContentFilter);

      List<DynamoDSContent> contents = mapper.scan(DynamoDSContent.class, scanContentExpression);
      for (DynamoDSContent dc : contents){
        portalContent.setChart(dc.getChart());
        portalContent.setQuery(dc.getQuery());
        portalContent.setId(dc.getId());
        portalContent.setTitle(dc.getTitle());
        portalContent.setDatePattern(dc.getDatePattern());
        portalContent.setAccessURL(dc.getAccessURL());
        portalContent.setDescription(dc.getDescription());
        portalContent.setSummary(dc.getSummary());
        portalContent.setSummaryHtml(portalContent.getSummaryHtml());
        portalContent.setDescriptionHtml(portalContent.getDescriptionHtml());
        portalContent.setDatasetId(portalContent.getDatasetId());
        if (dc.getPoligonId() != null){
          portalContent.setPoligon(getPoligon(dc.getPoligonId()));
        }
      }
    } catch (Throwable e) {
      logger.error("", e);
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
  
  public static List<Poligon> getPoligons(){
    List<Poligon> result = new ArrayList<Poligon>();
    try {
      DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
      DynamoDBScanExpression scanContentExpression = new DynamoDBScanExpression();
     
      List<DynamoDSPoligon> contents = mapper.scan(DynamoDSPoligon.class, scanContentExpression);
      for (DynamoDSPoligon dc : contents){
        Poligon poligon = new Poligon();
        poligon.setId(dc.getId());
        poligon.setTitle(dc.getTitle());
        poligon.setFilename(dc.getFilename());
        result.add(poligon);
      }
    } catch (Throwable e) {
      logger.error("", e);
    }
    return result;
  }

  public static void createPoligon(Poligon poligon){
    try {
      DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
      mapper.save(convertPoligon(poligon));
      
    } catch (Throwable e) {
      logger.error("", e);
    } 
  }
  
  public static void updatePoligon(Poligon poligon){
    try {
      DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
      mapper.save(convertPoligon(poligon));
      
    } catch (Throwable e) {
      logger.error("", e);
    } 
  }
  
  public static Poligon getPoligon(String poligonId){
    Poligon poligon = null;
    try {
      DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
      DynamoDBScanExpression scanContentExpression = new DynamoDBScanExpression();
      Map<String, Condition> scanContentFilter = new HashMap<String, Condition>();
      scanContentFilter.put("ID", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue().withS(poligonId)));
      scanContentExpression.setScanFilter(scanContentFilter);

      List<DynamoDSPoligon> contents = mapper.scan(DynamoDSPoligon.class, scanContentExpression);
      for (DynamoDSPoligon dc : contents){
        poligon = new Poligon();
        poligon.setId(dc.getId());
        poligon.setTitle(dc.getTitle());
        poligon.setFilename(dc.getFilename());
      }
      
    } catch (Throwable e) {
      logger.error("", e);
    }
    return poligon;
  }
  
 public static void deletePoligon(String poligonId) {
    try {
      DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
      DynamoDBScanExpression scanPortalExpression = new DynamoDBScanExpression();
      Map<String, Condition> scanPortalFilter = new HashMap<String, Condition>();
      scanPortalFilter.put("ID", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue().withS(poligonId)));
      scanPortalExpression.setScanFilter(scanPortalFilter);

      List<DynamoDSPoligon> portals = mapper.scan(DynamoDSPoligon.class, scanPortalExpression);
      mapper.batchDelete(portals);
    } catch (Throwable e) {
      logger.error("", e);
    }
  }
}
