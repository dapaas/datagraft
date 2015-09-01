package eu.dapaas.utils;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.Cookie;

import org.apache.http.impl.cookie.BasicClientCookie;

public class ApacheCookieUtils {
  public static Cookie servletCookieFromApacheCookie(org.apache.http.cookie.Cookie apacheCookie) {
    if(apacheCookie == null) {
     return null;
    }

    String name = apacheCookie.getName();
    String value = apacheCookie.getValue();

    Cookie cookie = new Cookie(name, value);

    // set the domain
    value = apacheCookie.getDomain();
    if(value != null) {
     cookie.setDomain(value);
    }

    // path
    value = apacheCookie.getPath();
    if(value != null) {
     cookie.setPath(value);
    }

    // secure
    cookie.setSecure(apacheCookie.isSecure());

    // comment
    value = apacheCookie.getComment();
    if(value != null) {
     cookie.setComment(value);
    }

    // version
    cookie.setVersion(apacheCookie.getVersion());

    // From the Apache source code, maxAge is converted to expiry date using the following formula
    // if (maxAge >= 0) {
          //     setExpiryDate(new Date(System.currentTimeMillis() + maxAge * 1000L));
          // }
    // Reverse this to get the actual max age

    Date expiryDate = apacheCookie.getExpiryDate();
    if(expiryDate != null) {
     long maxAge = (expiryDate.getTime() - System.currentTimeMillis()) / 1000;
     // we have to lower down, no other option
     cookie.setMaxAge((int) maxAge);
    }

    // return the servlet cookie
    return cookie;
   }

   /**
    * Method to convert a Java Servlet cookie to an Apache HttpClient cookie.
    * 
    * @param cookie the Java servlet cookie to convert
    * @return the Apache HttpClient cookie
    */
   public static org.apache.http.cookie.Cookie apacheCookieFromServletCookie(Cookie cookie) {
    if(cookie == null) {
     return null;
    }

    BasicClientCookie apacheCookie = null;

    // get all the relevant parameters
       String domain = cookie.getDomain();
       String name = cookie.getName();
       String value = cookie.getValue();
       String path = cookie.getPath();
       int maxAge = cookie.getMaxAge();
       boolean secure = cookie.getSecure();

       // create the apache cookie
       apacheCookie = new BasicClientCookie (name, value);
       apacheCookie.setDomain(domain);
       apacheCookie.setPath(path);
       if (maxAge > 0){
         Date expiryDate = new Date(Calendar.getInstance().getTime().getTime() + maxAge);
         apacheCookie.setExpiryDate(expiryDate);
       }
       
       apacheCookie.setSecure(secure);
       // set additional parameters
       apacheCookie.setComment(cookie.getComment());
       apacheCookie.setVersion(cookie.getVersion());

       // return the apache cookie
       return apacheCookie;
   }
}
