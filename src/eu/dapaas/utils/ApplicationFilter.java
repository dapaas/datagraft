package eu.dapaas.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import eu.dapaas.db.LocalDBProvider;

public class ApplicationFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    LocalDBProvider.destroyDB();
//    DynamoProvider.deleteDB();
    LocalDBProvider.createDB();
    
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    try {
      response.setContentType("text/html; charset=UTF-8");
      request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("UTF-8");
      filterChain.doFilter(request, response);
    } finally {

    }
  }

  @Override
  public void destroy() {
    LocalDBProvider.destroyDB();
  }
}
