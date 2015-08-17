package eu.dapaas.bean;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.Transformation;
import eu.dapaas.dao.TransformationMeta;
import eu.dapaas.dao.User;
import eu.dapaas.handler.DatasetHandler;
import eu.dapaas.handler.TransformationCatalogHandler;
import eu.dapaas.handler.TransformationHandler;

public class TransformationBean {
  private HttpServletResponse response;
  private HttpSession         session;
  private String              searchValue;
  private String              owner;
  private int                 pageCount = 0;
  private int                 pageNumber = 1;
  private static final int    PAGE_SIZE = 20;

  public List<Transformation> getCatalogTransformations(User user) {
    if (user == null){
      return new ArrayList<Transformation>();
    }
    TransformationCatalogHandler handler = new TransformationCatalogHandler(user.getApiKey(), user.getApiSecret());
    handler.setSearchValue(searchValue);
    List<Transformation> transformations = handler.getTransformationCatalog();
    return transformations;
  }

  public List<Transformation> getCatalogTransformations() {
    TransformationCatalogHandler handler = new TransformationCatalogHandler();
    handler.setSearchValue(searchValue);
    handler.setOwner(owner);
    List<Transformation> transformations = handler.getTransformationCatalog();
    return transformations;
  }

  public List<Transformation> getCatalogTransformationsByPage(int page) {
    TransformationCatalogHandler handler = new TransformationCatalogHandler();
    handler.setSearchValue(searchValue);
    handler.setOwner(owner);
    List<Transformation> transformations = handler.getTransformationCatalog();
    pageCount = calculatePages(transformations);
    pageNumber = page;
    List<Transformation> pageData = new ArrayList<Transformation>();
    int end = PAGE_SIZE*(pageNumber);
    if (end>=transformations.size()){
      end = transformations.size();
    }
    for (int i=PAGE_SIZE*(pageNumber-1); i<end; i++){
      pageData.add(transformations.get(i));
    }
    return pageData;
  }

  private int calculatePages(List list) {
    int maxPages = 0;
    if (PAGE_SIZE > 0) {
      if (list.size() % PAGE_SIZE == 0) {
        maxPages = list.size() / PAGE_SIZE;
      } else {
        maxPages = (list.size() / PAGE_SIZE) + 1;
      }
    }
    return maxPages;
  }
  
  public List<Transformation> getScharedTransformations(User user) {
    if (user == null){
      return new ArrayList<Transformation>();
    }
    TransformationCatalogHandler handler = new TransformationCatalogHandler(user.getApiKey(), user.getApiSecret());
    handler.setSearchValue(searchValue);
    handler.setOwner(owner);
    List<Transformation> transformations = handler.getSharedTransformationCatalog();
    return transformations;
  }

  public Transformation getDetail(User user, String id) {
    TransformationCatalogHandler handler = new TransformationCatalogHandler();
    if (user != null){
      handler = new TransformationCatalogHandler(user.getApiKey(), user.getApiSecret());
    }
    return handler.getDetail(id);
  }



  public void delete(User user, String id) {
    if (user == null){
      return ;
    }
    TransformationHandler header = new TransformationHandler(user.getApiKey(), user.getApiSecret());
    header.deleteTransformation(id);
  }

//  public void forkTransformation(String id){
//    if (user == null){
//      return;
//    }
//    TransformationHandler header = new TransformationHandler(user.getApiKey(), user.getApiSecret());
//    TransformationCatalogHandler handlercatalog = new TransformationCatalogHandler(user.getApiKey(), user.getApiSecret());
//    Transformation transformation = handlercatalog.getDetail(id);
//    TransformationMeta transformationMeta = new TransformationMeta();
//    
//    transformationMeta.setDescription(transformation.getDescription());
//    transformationMeta.setPublic(transformation.isPublic());
//    transformationMeta.setTitle(transformation.getTitle());
//    transformationMeta.setTransformationCommand(transformation.getTransformationCommand());
//    transformationMeta.setTransformationType(transformation.getTransformationType());
//    
//    header.getClojureFile(id);
//    header.getJsonFile(id);
////    header.createTransformation(transformationMeta, clojure);
//  }
  
  
  public void executeAndDownload() {
    try {
      User user = (User) session.getAttribute(SessionConstants.DAPAAS_USER);
      Wizard wizard = (Wizard) session.getAttribute("wizard");
      DatasetHandler handler = new DatasetHandler(wizard, user);
      File file = handler.executeAndDownload();
      if (file != null) {
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
        int i;
        while ((i = fileInputStream.read()) != -1) {
          response.getOutputStream().write(i);
        }
        fileInputStream.close();
        response.getOutputStream().close();
        file.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public HttpServletResponse getResponse() {
    return response;
  }

  public void setResponse(HttpServletResponse response) {
    this.response = response;
  }

  public HttpSession getSession() {
    return session;
  }

  public void setSession(HttpSession session) {
    this.session = session;
  }

  public String getSearchValue() {
    return searchValue;
  }

  public void setSearchValue(String searchValue) {
    this.searchValue = searchValue;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public int getPageCount() {
    return pageCount;
  }

  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }
}
