/**
 * 
 */
package eu.dapaas.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * @author dzhelil
 * 
 */
public abstract class BaseHandler {
  private static final Logger logger = Logger.getLogger(BaseHandler.class);

  private HttpServletResponse response;
  private HttpSession         session;

  protected void redirectToPage(String param) throws IOException, ServletException {
    getResponse().sendRedirect(param);
  }

  protected void redirectToPage(String param, JSONObject object) throws IOException, ServletException {
    getResponse().sendRedirect(param);
  }

  protected void printOut(Object object) throws IOException {
    PrintWriter out = null;
    out = getResponse().getWriter();
    getResponse().setContentType("application/json");
    out.print(object);
    out.flush();
  }

  protected void showErrorPage(String error) {
    getSession().setAttribute("error", error);
    try {
      getResponse().sendRedirect("pages/error");
    } catch (IOException e) {
      logger.error("", e);
    }
  }

  public HttpServletResponse getResponse() {
    return response;
  }

  public void setResponse(HttpServletResponse response) {
    this.response = response;
  }

  public HttpSession getSession() {
    return this.session;
  }

  public void setSession(HttpSession session) {
    this.session = session;
  }
}
