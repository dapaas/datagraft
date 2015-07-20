package eu.dapaas.bean;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.Transformation;
import eu.dapaas.dao.User;
import eu.dapaas.handler.DatasetHandler;
import eu.dapaas.handler.TransformationCatalogHandler;
import eu.dapaas.handler.TransformationHandler;

public class TransformationBean {
  private HttpServletResponse response;
  private HttpSession         session;
  private String              searchValue;

  public List<Transformation> getCatalogTransformations(User user) {
    TransformationCatalogHandler handler = new TransformationCatalogHandler(user.getApiKey(), user.getApiSecret());
    handler.setSearchValue(searchValue);
    List<Transformation> transformations = handler.getTransformationCatalog();
    return transformations;
  }

  public List<Transformation> getScharedTransformations(User user) {
    TransformationCatalogHandler handler = new TransformationCatalogHandler(user.getApiKey(), user.getApiSecret());
    handler.setSearchValue(searchValue);
    List<Transformation> transformations = handler.getSharedTransformationCatalog();
    return transformations;
  }

  public Transformation getDetail(User user, String id) {
    TransformationCatalogHandler handler = new TransformationCatalogHandler(user.getApiKey(), user.getApiSecret());
    return handler.getDetail(id);
  }

  public void delete(User user, String id) {
    TransformationHandler header = new TransformationHandler(user.getApiKey(), user.getApiSecret());
    header.deleteTransformation(id);
  }

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
}
