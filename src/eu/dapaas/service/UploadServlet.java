package eu.dapaas.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.json.JSONArray;
import org.json.JSONObject;

import eu.dapaas.bean.Wizard;
import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.UploadFile;
import eu.dapaas.dao.User;
import eu.dapaas.utils.Config;
import eu.dapaas.utils.Guid;

//@WebServlet("/upload")
//@
public class UploadServlet extends HttpServlet {

  private static final Logger logger = Logger.getLogger(UploadServlet.class);

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    PrintWriter writer = response.getWriter();
    writer.write("call POST with multipart form data");
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    response.addHeader("content-type", "application/json");
    response.setCharacterEncoding("UTF-8");
    if (!ServletFileUpload.isMultipartContent(request)) {
      //throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request is not multipart, please 'multipart/form-data' enctype for your form.");
      return;
    }

    JSONArray json = new JSONArray();
    JSONObject files = new JSONObject();
    
    HttpSession session = request.getSession();
    User user = (User) session.getAttribute(SessionConstants.DAPAAS_USER);
    if (user == null){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setHeader("Content-Type", "application/json");
      response.getOutputStream().print("{\"error\":\"File can not upload. Please sign in and try again.\"}");
      response.flushBuffer();
      return; 
    }
    Wizard wizard = (Wizard) session.getAttribute("wizard");

    File fileUploadPath = new File(Config.getInstance().getPathUploadFile() + File.separator + user.getUsername());
    if (!fileUploadPath.exists()) {
      fileUploadPath.mkdir();
    }
    ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
    PrintWriter writer = response.getWriter();

    try {
      UploadFile uploadFile = new UploadFile();
      ServletRequestContext requestContext = new ServletRequestContext(request);
      List<FileItem> items = uploadHandler.parseRequest(requestContext);
      for (FileItem item : items) {
        if (!item.isFormField()) {
          File file = new File(fileUploadPath, item.getName());
          item.write(file);
          uploadFile.setFile(file);
          logger.debug("file uploaded with name " + file.getParentFile() + "/" + file.getName());
        } else {
//          if (item.getFieldName().equals("index")) {
//            uploadFile.setIndex(item.getString());
//          }
          if (item.getFieldName().equals("filetype")) {
            uploadFile.setFiletype(item.getString());
            String guid = Guid.getNewGuid();
            guid = guid.substring(0, 5);
            uploadFile.setIndex(guid);
          }
          if (item.getFieldName().equals("contenttype")) {
            uploadFile.setContentType(item.getString());
          }
          if (item.getFieldName().equals("transformation")) {
            uploadFile.setTransformation(item.getString());
          }
          
        }
      }
      wizard.setUploadesFile(uploadFile);
      session.setAttribute("wizard", wizard);
      // edit||add
      JSONObject jsono = new JSONObject();
      jsono.put("file", uploadFile.getFile().getName());
      jsono.put("index", uploadFile.getIndex());
      jsono.put("filetype", uploadFile.getFiletype());
      json.put(jsono);
      files.put("result", json);
    } catch (FileUploadException e) {
      logger.error("", e);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.setHeader("Content-Type", "application/json");
      response.getOutputStream().print("{\"error\":\"File can not upload.\"}");
      response.flushBuffer();
      return; 
    } catch (Exception e) {
      logger.error("", e);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.setHeader("Content-Type", "application/json");
      response.getOutputStream().print("{\"error\":\"File can not upload.\"}");
      response.flushBuffer();
      return; 
    } finally {

      writer.write(files.toString());
      writer.close();
    }
  }
}
