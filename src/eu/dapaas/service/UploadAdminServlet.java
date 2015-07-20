package eu.dapaas.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;

import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.Poligon;
import eu.dapaas.dao.User;
import eu.dapaas.db.LocalDBProvider;
import eu.dapaas.utils.Config;
import eu.dapaas.utils.Guid;
import eu.dapaas.utils.Utils;

@WebServlet("/admin")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
public class UploadAdminServlet extends HttpServlet {
  private static final Logger logger = Logger.getLogger(UploadAdminServlet.class);

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    PrintWriter writer = response.getWriter();
    writer.write("call POST with multipart form data");
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    if (!ServletFileUpload.isMultipartContent(request)) {
      throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
    }

    JSONArray json = new JSONArray();
    JSONObject files = new JSONObject();

    HttpSession session = request.getSession();
    User user = (User) session.getAttribute(SessionConstants.DAPAAS_USER);

    File fileUploadPath = new File(Config.getInstance().getPathUploadFile());
    if (!fileUploadPath.exists()) {
      fileUploadPath.mkdir();
    }

    PrintWriter writer = response.getWriter();
    try {
      Poligon poligon = new Poligon();
      for (Part part : request.getParts()) {
        String name = extractName(part);
        if (name.equals("id")) {
          String id = getValue(part);
          poligon.setId(id);
        }

        if (name.equals("title")) {
          String title = getValue(part);
          poligon.setTitle(title);
        }
        if (name.equals("inputFile")) {
          String guid = Guid.getNewGuid();
          guid = guid.substring(0, 5);
          String fileName = extractFileName(part);
          if (!Utils.isEmpty(fileName)) {
            part.write(fileUploadPath + File.separator + guid + fileName);
            poligon.setFilename(guid + fileName);
          }
        }

      }
      // TODO: check file !!! .json,

      // {"features":[
      // {"properties":{"NAME": * , "CODE": * },
      // "type":"Feature",
      // "geometry":{"type":"Polygon",
      // "coordinates":[[[*]]]
      // }
      // }

      if (Utils.isEmpty(poligon.getId())) {
        LocalDBProvider.createPoligon(poligon);
      } else {
        if (!Utils.isEmpty(poligon.getFilename())) {
          Poligon old = LocalDBProvider.getPoligon(poligon.getId());
          File oldFile = new File(Config.getInstance().getPathUploadFile() + File.separator + old.getFilename());
          oldFile.delete();
        }
        LocalDBProvider.updatePoligon(poligon);
      }
      JSONObject jsono = new JSONObject();
      if (!Utils.isEmpty(poligon.getFilename())) {
        jsono.put("file", poligon.getFilename());
        json.put(jsono);
        files.put("result", json);
        response.sendRedirect("pages/admin/upload.jsp");
      } else {
        files.put("error", "File not uploaded. Please upload new file");
        response.sendRedirect("pages/error.jsp");
      }

    } catch (Exception e) {
      logger.error("", e);
    } finally {

      writer.write(files.toString());
      writer.close();
    }
  }

  /**
   * Extracts file name from HTTP header content-disposition
   */
  private String extractFileName(Part part) {
    String contentDisp = part.getHeader("content-disposition");
    String[] items = contentDisp.split(";");
    for (String s : items) {
      if (s.trim().startsWith("filename")) {
        return s.substring(s.indexOf("=") + 2, s.length() - 1);
      }
    }
    return "";
  }

  private String extractName(Part part) {
    String contentDisp = part.getHeader("content-disposition");
    String[] items = contentDisp.split(";");
    for (String s : items) {
      if (s.trim().startsWith("name")) {
        return s.substring(s.indexOf("=") + 2, s.length() - 1);
      }
    }
    return "";
  }

  private String getValue(Part part) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
    StringBuilder value = new StringBuilder();
    char[] buffer = new char[1024];
    for (int length = 0; (length = reader.read(buffer)) > 0;) {
      value.append(buffer, 0, length);
    }
    return value.toString();
  }
}
