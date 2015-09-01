package eu.dapaas.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;

import com.sirmamobile.encrypt.Base64;

import eu.dapaas.constants.SessionConstants;
import eu.dapaas.dao.User;
import eu.dapaas.handler.UserHandler;
import eu.dapaas.http.HttpMethod;
import eu.dapaas.http.impl.DaPaasParams;
import eu.dapaas.http.impl.DaPaasUserGateway;
import eu.dapaas.utils.ApacheCookieUtils;
import eu.dapaas.utils.Config;
import eu.dapaas.utils.Utils;

@WebServlet("/confirm")
public class EmailConfirmListener extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      doPost(request, response);
    } catch (ServletException s) {
      s.printStackTrace();
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Enumeration<String> params = request.getParameterNames();
    while (params.hasMoreElements()) {
      String string = (String) params.nextElement();
      String decodelink = new String(Base64.decode(string+"="));
      if (decodelink != null) {
        String[] array = decodelink.split(";");
        
        String email = array[0];
        String time = array[1];
        String username = array[2];
        
        Calendar today = Calendar.getInstance();
        Calendar linkdate = Calendar.getInstance();
        linkdate.setTimeInMillis(new Long(time));
        today.add(Calendar.HOUR, -1 * new Integer(Config.getInstance().getConfirmLinkExpireHour()));
        // 2h exiration
        if (linkdate.before(today)) {
          request.getSession().setAttribute(SessionConstants.ERROR, "Sorry, this link was expired.");
          request.getRequestDispatcher("/pages/error").forward(request, response);
          break;
        }
        // TODO; need to check this email with username if exist in the system.
        //
        User userdetails = new User();
        DaPaasParams dapaasParams = new DaPaasParams();
        dapaasParams.getHeaders().put("Content-Type", "application/json");
        
        DaPaasUserGateway gateway = new DaPaasUserGateway(HttpMethod.GET, Utils.getDaPaasEndpoint("dapaas-management-services/api/accounts/details"), dapaasParams);
        User user = (User) request.getSession().getAttribute(SessionConstants.DAPAAS_USER);
        if (user != null){
          for (Cookie cookie : user.getCookies()) {
            gateway.getContext().getCookieStore().addCookie(cookie);
          }
          HttpResponse httpresponse = gateway.execute();
          userdetails.setUser(Utils.convertEntityToJSON(httpresponse));
          if (userdetails.getEmail().equals(email) && userdetails.getUsername().equals(username)){
            user.setConfirm(true);
            request.getSession().setAttribute(SessionConstants.DAPAAS_USER, user);
            //FIXME: need new api implementation
//            UserHandler handler = new UserHandler(request, response, request.getSession()); //
//            try{
//              handler.updateUserDetail(user);
//            }catch(Exception e){
//              e.printStackTrace();
//            }
            
            request.getRequestDispatcher("/pages/publish").forward(request, response);
          }else{
            request.getSession().setAttribute(SessionConstants.ERROR, "Sorry, email confirmation not for this user!");
            request.getRequestDispatcher("/pages/error").forward(request, response);
          }
        }else{
          request.getRequestDispatcher("/pages/signin.jsp").forward(request, response);
        }
      }
    }
  }
}
