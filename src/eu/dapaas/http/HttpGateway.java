package eu.dapaas.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

/**
 *
 */
public abstract class HttpGateway {

  private static final Logger logger = Logger.getLogger(HttpGateway.class);

  protected HttpClientBuilder clientBuilder;
  protected HttpMethod        method;
  protected String            url;

  public HttpGateway(HttpMethod method, String url) {
    this.clientBuilder = HttpClientBuilder.create();
    this.method = method;
    this.url = url;
    assertFollowRedirects();
  }

  public HttpResponse execute() throws IOException {
    HttpResponse result = null;
    switch (method.toString()) {
    case "PUT":
      result = put();
      break;
    case "POST":
      result = post();
      break;
    case "GET":
      result = get();
      break;
    case "DELETE":
      result = delete();
      break;
    default:
      break;
    }
    return result;
  }

  protected abstract HttpResponse post() throws IOException;

  protected abstract HttpResponse get() throws IOException;

  protected abstract HttpResponse put() throws IOException;

  protected abstract HttpResponse delete() throws IOException;


  private void assertFollowRedirects() {
    this.clientBuilder.setRedirectStrategy(new DefaultRedirectStrategy() {
      public boolean isRedirected(org.apache.http.HttpRequest request, HttpResponse response, HttpContext context) {
        boolean isRedirect = false;
        try {
          isRedirect = super.isRedirected(request, response, context);
        } catch (ProtocolException e) {
          logger.error(e.getMessage(), e);
        }
        if (!isRedirect) {
          int responseCode = response.getStatusLine().getStatusCode();
          if (responseCode == 301 || responseCode == 302) {
            return true;
          }
        }
        return isRedirect;
      }
    });
  }
  
  protected byte[] read(File file) throws IOException {
    ByteArrayOutputStream ous = null;
    InputStream ios = null;
    try {
      byte[] buffer = new byte[4096];
      ous = new ByteArrayOutputStream();
      ios = new FileInputStream(file);
      int read = 0;
      while ((read = ios.read(buffer)) != -1) {
        ous.write(buffer, 0, read);
      }
    } finally {
      try {
        if (ous != null)
          ous.close();
      } catch (IOException e) {
      }

      try {
        if (ios != null)
          ios.close();
      } catch (IOException e) {
      }
    }
    return ous.toByteArray();
  }
}
