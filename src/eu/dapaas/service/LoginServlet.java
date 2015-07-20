package eu.dapaas.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.exception.OAuthException;

import org.apache.log4j.Logger;

import com.neurologic.oauth.config.ModuleConfig;
import com.neurologic.oauth.util.Globals;

import eu.dapaas.handler.LoginHandler;
import eu.dapaas.oauth.FacebookOAuthService;
import eu.dapaas.oauth.GoogleOAuthService;
import eu.dapaas.oauth.TwitterOAuthService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  private static final Logger logger = Logger.getLogger(LoginServlet.class);

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      doPost(request, response);
    } catch (ServletException s) {
      s.printStackTrace();
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("action");
    ModuleConfig moduleConfig = (ModuleConfig) request.getSession().getServletContext().getAttribute(Globals.MODULE_KEY);
    switch (action) {
    case "submitFB":
      try {
        String url = FacebookOAuthService.getOAuthLoginUrl(moduleConfig, request);
        response.sendRedirect(url);
      } catch (OAuthException e) {
        logger.error("", e);
      }
      break;
    case "submitT":
      try {
        String url = TwitterOAuthService.getOAuthLoginUrl(moduleConfig, request);
        response.sendRedirect(url);
      } catch (OAuthException e) {
        logger.error("", e);
      }
      break;
    case "submitG":
      try {
        String url = GoogleOAuthService.getOAuthLoginUrl(moduleConfig, request);
        response.sendRedirect(url);
      } catch (OAuthException e) {
        logger.error("", e);
      }
      break;
    default:
      LoginHandler hendler = new LoginHandler(request, response, request.getSession());
      hendler.login(request.getParameter("username"), request.getParameter("password"));
      break;
    }
  }
}
