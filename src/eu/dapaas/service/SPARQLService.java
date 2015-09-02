package eu.dapaas.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.dapaas.bean.Wizard;
import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.User;
import eu.dapaas.handler.QueryHandler;

@WebServlet("/executeQuery")
public class SPARQLService extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
    try {
      doPost(request, response);
    } catch (ServletException s) {
      s.printStackTrace();
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // try{
    String query = request.getParameter("query");
    Wizard wizard = (Wizard) request.getSession().getAttribute("wizard");
    User user = (User) request.getSession().getAttribute(SessionConstants.DAPAAS_USER);
    QueryHandler queryhandler = new QueryHandler(query);
    try{
      String responseStr = queryhandler.executeQueryById(user.getApiKey(), user.getApiSecret(), wizard.getDetails().getId(), "application/sparql-results+xml");
      response.setContentType("application/sparql-results+xml; charset=UTF-8");
      PrintWriter printout = response.getWriter();
      printout.write(responseStr);
      printout.flush();
    }catch(Exception e){
      e.printStackTrace();
    }
  }
}
