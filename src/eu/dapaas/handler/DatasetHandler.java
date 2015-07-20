package eu.dapaas.handler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import eu.dapaas.bean.Wizard;
import eu.dapaas.dao.Dataset;
import eu.dapaas.dao.DistributionDetail;
import eu.dapaas.dao.DistributionMeta;
import eu.dapaas.dao.PortalContent;
import eu.dapaas.dao.Repository;
import eu.dapaas.dao.Transformation;
import eu.dapaas.dao.UploadFile;
import eu.dapaas.dao.User;
import eu.dapaas.db.LocalDBProvider;
import eu.dapaas.http.HttpMethod;
import eu.dapaas.http.NameValuePair;
import eu.dapaas.http.impl.DaPaasGateway;
import eu.dapaas.http.impl.DaPaasParams;
import eu.dapaas.utils.Utils;

/*{"@context":
 *  {"dcat":"http://www.w3.org/ns/dcat#",
 *    "foaf":"http://xmlns.com/foaf/0.1/",
 *    "dct":"http://purl.org/dc/terms/",
 *    "xsd":"http://www.w3.org/2001/XMLSchema#",
 *    "dct:issued":{"@type":"xsd:date"},
 *    "dct:modified":{"@type":"xsd:date"},
 *    "foaf:primaryTopic":{"@type":"@id"},
 *    "dcat:distribution":{"@type":"@id"}
 *   },
 *   "@type":"dcat:Dataset",
 *   "@id":"http://eu.dapaas/dataset/2",
 *   "dct:issued":"2014-09-15",
 *   "dct:modified":"2014-09-17",
 *   "dct:title":"My second DaPaaS dataset",
 *   "dct:description":"Sample DS 2 descr",
 *   "dct:publisher":"Alex",
 *   "dcat:keyword":[],
 *   "dcat:distribution":[]
 * }*/
public class DatasetHandler extends BaseHandler {
  private static final Logger logger                = Logger.getLogger(DatasetHandler.class);
  private Wizard              wizard;
  private User                user;
  private DaPaasGateway       gateway               = null;
  private String              publisher             = null;
  private String              error;
  private static final int    MAX_COUNT_OF_WAITHING = 15;// 120;

  public DatasetHandler(Wizard wizard, User user) {
    this.wizard = wizard;
    this.user = user;
  }

  public DatasetHandler(User user) {
    this.wizard = null;
    this.user = user;
  }

  public Wizard createDataset() {
    try {
      Dataset datasetCatalogDetails = wizard.getDetails();

      // before create need to check PORTAL PARAM ( will be unique ). check by
      // portal_parameter and null
      datasetCatalogDetails.setIssued(formatCurrentDate());

      DaPaasParams params = new DaPaasParams();
      params.setJsonObject(new NameValuePair<JSONObject>(null, datasetCatalogDetails.toJSON()));
      gateway = new DaPaasGateway(HttpMethod.POST, user.getApiKey(), user.getApiSecret(), Utils.getDaPaasEndpoint("catalog/datasets"), params);
      JSONObject serverResponse = Utils.convertEntityToJSON(gateway.execute());
      logger.debug("CREATE DATASET : " + serverResponse);
      String datasetId = serverResponse.get("@id").toString();
      wizard.getDetails().setId(datasetId);
      JSONObject jemptycontext = new JSONObject();
      jemptycontext.put("@context", new JSONObject());

      UploadFile file = wizard.getUploadesFile();
      String distibutionId = "";
      String accessURL = "";
      switch (file.getFiletype()) {
      case "RDF": {
        distibutionId = createRDFDistribution(datasetId, datasetCatalogDetails.getTitle(), datasetCatalogDetails.getDescription(), datasetCatalogDetails.getKeyword());
        accessURL = createRepository(distibutionId);
        uploadFiles(file, accessURL, user.getUsername());
      }
        break;
      case "GRF": {
        distibutionId = createGRFDistribution(datasetId, datasetCatalogDetails.getTitle(), datasetCatalogDetails.getDescription(), file);
        accessURL = createRepository(distibutionId);
        Transformation transformation = wizard.getTransformation();
        transformation(transformation, distibutionId, datasetCatalogDetails.getTitle(), file);
      }
        break;
      }
      wizard.getDetails().getDistribution().add(distibutionId);
      wizard.getDetails().setAccessURL(accessURL);

      // Go to embebed DB and add rows for this DS with ID : datasetId
      wizard.getPortal().setDatasetId(datasetId);
      for (PortalContent pc : wizard.getPortal().getPortalContent()) {
        pc.setAccessURL(accessURL);
      }

      if (wizard.getPortal().getParameter() != null && wizard.getPortal().getParameter().length() > 0 && wizard.getPortal().getPortalContent().size() > 0) {
        // insert only
        if (LocalDBProvider.checkPortalParameter(wizard.getPortal().getParameter(), null)) {
          throw new Exception("Can't create portal because parameter exist");
        }
        if (wizard.getPortal().getPortalContent().size() > 0) {
          LocalDBProvider.insertDSPortal(wizard.getPortal());
        }
      }

    } catch (Exception e) {
      error = e.getMessage();
      logger.error("", e);
    }
    return wizard;

  }

  public Wizard updateDataset() {
    try {

      Dataset datasetCatalogDetails = wizard.getDetails();
      datasetCatalogDetails.setModified(formatCurrentDate());

      DaPaasParams params = new DaPaasParams();
      params.setJsonObject(new NameValuePair<JSONObject>(null, datasetCatalogDetails.toJSON()));
      gateway = new DaPaasGateway(HttpMethod.PUT, user.getApiKey(), user.getApiSecret(), Utils.getDaPaasEndpoint("catalog/datasets"), params);
      JSONObject serverResponse = Utils.convertEntityToJSON(gateway.execute());

      logger.debug("UPDATE DATASET : " + serverResponse);
      String datasetId = datasetCatalogDetails.getId(); 

      JSONObject jemptycontext = new JSONObject();
      jemptycontext.put("@context", new JSONObject());

      String distibutionId = "";
      String accessURL = "";
      if (datasetCatalogDetails.getDistribution().size() > 0 && datasetCatalogDetails.getDistribution().get(0).length() > 0) {
        distibutionId = datasetCatalogDetails.getDistribution().get(0).toString();
        params = new DaPaasParams();
        params.setJsonObject(new NameValuePair<JSONObject>(null, jemptycontext));
        params.getHeaders().put("distrib-id", distibutionId);
        gateway.modifiedDaPaasGateway(HttpMethod.GET, Utils.getDaPaasEndpoint("catalog/distributions"), params);
        JSONObject response3 = Utils.convertEntityToJSON(gateway.execute());

        logger.debug("GET distributions : " + response3);
        DistributionDetail distribution = new DistributionDetail(response3);

        accessURL = distribution.getAccessURL();
      } else {
        // create
        UploadFile file = wizard.getUploadesFile();
        switch (file.getFiletype()) {
        case "RDF": {
          distibutionId = createRDFDistribution(datasetId, datasetCatalogDetails.getTitle(), datasetCatalogDetails.getDescription(), datasetCatalogDetails.getKeyword());
          accessURL = createRepository(distibutionId);
          
        }
          break;
        case "GRF": {
          distibutionId = createGRFDistribution(datasetId, datasetCatalogDetails.getTitle(), datasetCatalogDetails.getDescription(), file);
          accessURL = createRepository(distibutionId);
        }
          break;
        }
      }

      wizard.getDetails().setAccessURL(accessURL);

      UploadFile file = wizard.getUploadesFile();
      if (file.getFile() != null) {
        switch (file.getFiletype()) {
        case "RDF": {
          uploadFiles(file, accessURL, user.getUsername());
        }
          break;
        case "GRF": {
          Transformation transformation = wizard.getTransformation();
          transformation(transformation, distibutionId, datasetCatalogDetails.getTitle(), file);
        }
          break;
        }
      }

      wizard.getPortal().setDatasetId(datasetId);
      for (PortalContent pc : wizard.getPortal().getPortalContent()) {
        pc.setAccessURL(accessURL);
      }
      // insert or update
      if (LocalDBProvider.checkPortalParameter(wizard.getPortal().getParameter(), datasetId)) {
        throw new Exception("Cant update portal because parameter exist");
      }
      if (wizard.getPortal().getPortalContent().size() > 0) {
        LocalDBProvider.deleteDSPortal(datasetId);
        LocalDBProvider.insertDSPortal(wizard.getPortal());
      }
    } catch (Exception e) {
      error = e.getMessage();
      logger.error("", e);
    }
    return wizard;

  }

  private String createRepository(String distibutionId) throws Exception{
    String accessURL = null;
    JSONObject jemptycontext = new JSONObject();
    jemptycontext.put("@context", new JSONObject());
    DaPaasParams params = new DaPaasParams();
    params.setJsonObject(new NameValuePair<JSONObject>(null, jemptycontext));
    params.getHeaders().put("distrib-id", distibutionId);
    gateway.modifiedDaPaasGateway(HttpMethod.PUT, Utils.getDaPaasEndpoint("/catalog/distributions/repository"), params); //repository_asynch
    HttpResponse repositoryResponse = gateway.execute();
    // JSONObject response3 = Utils.convertEntityToJSON();

    // wait to create repository if repositoryResponse 200
    if (repositoryResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      int i = 0;
      while (true) {
        try {

          params = new DaPaasParams();
          params.setJsonObject(new NameValuePair<JSONObject>(null, jemptycontext));
          params.getHeaders().put("distrib-id", distibutionId);
          gateway.modifiedDaPaasGateway(HttpMethod.GET, Utils.getDaPaasEndpoint("catalog/distributions"), params);
          JSONObject response3 = Utils.convertEntityToJSON(gateway.execute());

          logger.debug("GET distributions : " + response3);
          DistributionDetail distribution = new DistributionDetail(response3);

          accessURL = distribution.getAccessURL();

          if (!Utils.isEmpty(accessURL)) {
            break;
          }
          if (i >= MAX_COUNT_OF_WAITHING) {
            error = "We wait too long. Timeout exception! ";
            break;
          }
          i++;
          Thread.sleep(2000); // 1000 milliseconds is one second.
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
          error = ex.getMessage();
          break;
        }
      }
    }
    if (error != null){
      throw new Exception(error);
    }
    return accessURL;
  }
  
  private void transformation(Transformation transformation, String distibutionId, String title, UploadFile file) throws Exception{

    if ("pipe".equals(transformation.getTransformationType())) {
      DaPaasParams params = new DaPaasParams();
      params.setMultipart(true);
      params.getHeaders().put("result-distribution", distibutionId);
      params.getHeaders().put("result-file", title);
      params.getHeaders().put("result-type", "text/csv");
      params.getHeaders().put("transformation-id", transformation.getId());
      params.setFile(new NameValuePair<File>("input-file", file.getFile()));
      gateway.modifiedDaPaasGateway(HttpMethod.POST, Utils.getDaPaasEndpoint("dapaas-services/grafter/transformation/pipe"), params);
    }
    if ("graft".equals(transformation.getTransformationType())) {
      DaPaasParams params = new DaPaasParams();
      params.setMultipart(true);
      params.getHeaders().put("result-distribution", distibutionId);
      params.getHeaders().put("transformation-id", transformation.getId());
      params.setFile(new NameValuePair<File>("input-file", file.getFile()));

      gateway.modifiedDaPaasGateway(HttpMethod.POST, Utils.getDaPaasEndpoint("dapaas-services/grafter/transformation/graft"), params);

    }
    gateway.execute();

  }
  
  private String createRDFDistribution(String datasetId, String title, String description, List<String> keywords) throws Exception{
    DaPaasParams params = new DaPaasParams();
    params.getHeaders().put("dataset-id", datasetId);

    DistributionMeta metadata = new DistributionMeta(new JSONObject());
    metadata.setTitle(title);
    metadata.setDescription(description);
    metadata.setKeyword(keywords);
    metadata.setIssued(formatCurrentDate());
    metadata.setModified(formatCurrentDate());
    params.setMultipart(true);

    params.setJsonObject(new NameValuePair<JSONObject>("meta", metadata.toJSON()));
    gateway.modifiedDaPaasGateway(HttpMethod.POST, Utils.getDaPaasEndpoint("catalog/distributions"), params);
    JSONObject response2 = Utils.convertEntityToJSON(gateway.execute());
    return response2.get("@id").toString();

  }
  private String createGRFDistribution(String datasetId, String title, String description, UploadFile file) throws Exception{
    JSONObject jemptycontext = new JSONObject();
    jemptycontext.put("@context", new JSONObject());
    DaPaasParams params = new DaPaasParams();
    params.setJsonObject(new NameValuePair<JSONObject>(null, jemptycontext));
    params.getHeaders().put("dataset-id", datasetId);
    DistributionMeta metadata = new DistributionMeta(new JSONObject());
    metadata.setTitle(title);
    metadata.setDescription(description);
    metadata.setFileName(file.getFile().getName());
    metadata.setMediaType(file.getContentType()); // file.getContentType()
    metadata.setIssued(formatCurrentDate());
    metadata.setModified(formatCurrentDate());
    params.setFile(new NameValuePair<File>("file", file.getFile()));
    params.setJsonObject(new NameValuePair<JSONObject>("meta", metadata.toJSON()));
    params.setMultipart(true);
    gateway.modifiedDaPaasGateway(HttpMethod.POST, Utils.getDaPaasEndpoint("catalog/distributions"), params);
    JSONObject response2 = Utils.convertEntityToJSON(gateway.execute());

    logger.debug("CREATE distributions : " + response2);
    return response2.get("@id").toString();
  }

  public File executeAndDownload() {
    try {

      Transformation transformation = wizard.getTransformation();
      UploadFile file = wizard.getUploadesFile();
      String fileExtention = "";
      DaPaasParams params = new DaPaasParams();
      if ("pipe".equals(transformation.getTransformationType())) {
        params.getHeaders().put("Accept", "application/edn");
        fileExtention = ".edn";
      }
      if ("graft".equals(transformation.getTransformationType())) {
        params.getHeaders().put("Accept", "application/n-triples");
        fileExtention = ".nt";
      }
      params.setMultipart(true);

      params.getHeaders().put("transformation-id", transformation.getId());
      params.setFile(new NameValuePair<File>("input-file", file.getFile()));
      gateway = new DaPaasGateway(HttpMethod.POST, user.getApiKey(), user.getApiSecret(), Utils.getDaPaasEndpoint("dapaas-services/grafter/transformation/preview"), params);
      // gateway.execute();

      HttpResponse response = gateway.execute();

      String tempFolder = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
      InputStream ins = null;
      OutputStream out = null;
      File tempfile = null;
      try {
        File dir = new File(tempFolder + File.separator + user.getUsername());
        if (!dir.exists()) {
          dir.mkdir();
        }
        ins = response.getEntity().getContent();
        tempfile = new File(tempFolder + File.separator + user.getUsername() + File.separator + user.getUsername() + fileExtention);
        if (tempfile.exists()) {
          tempfile.delete();
        }
        out = new FileOutputStream(tempfile);
        byte[] buf = new byte[1024];
        int len;
        while ((len = ins.read(buf)) > 0) {
          out.write(buf, 0, len);
        }
        ins.close();
        out.close();
      } finally {
        if (ins != null)
          ins.close();
        if (out != null)
          out.close();
      }
      return tempfile;
    } catch (Exception e) {
      logger.error("", e);
    }
    return null;

  }

  private byte[] read(File file) throws IOException {
    ByteArrayOutputStream ous = null;
    InputStream ios = null;
    try {
      byte[] buffer = new byte[4096];
      ous = new ByteArrayOutputStream();
      ios = new FileInputStream(file);
      int read = 0;
      while ((read = ios.read(buffer)) != -1) {
        ous.write(buffer, 0, read);
      }
    } finally {
      try {
        if (ous != null)
          ous.close();
      } catch (IOException e) {
      }

      try {
        if (ios != null)
          ios.close();
      } catch (IOException e) {
      }
    }
    return ous.toByteArray();
  }

  private String formatCurrentDate() {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date today = Calendar.getInstance().getTime();
    String reportDate = df.format(today);
    return reportDate;
  }

  private void uploadFiles(UploadFile file, String accessURL, String username) throws IOException {
    if (Utils.isEmpty(accessURL)) {
      return;
    }
    switch (file.getFiletype()) {
    case "RDF": {
      try {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", file.getContentType());
        DaPaasParams params = new DaPaasParams();
        params.setHeaders(headers);
        params.setFile(new NameValuePair<File>(null, file.getFile()));
        params.setMultipart(false);
        gateway.modifiedDaPaasGateway(HttpMethod.POST, accessURL + "/statements", params);
        HttpResponse result = gateway.execute();

        JSONObject responseRDF = Utils.convertEntityToJSON(result);
        logger.debug("RESULT upload RDF : " + responseRDF);
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }
      break;
    }
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
