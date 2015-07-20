package eu.dapaas.http.impl;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import eu.dapaas.http.HttpGateway;
import eu.dapaas.http.HttpMethod;
// DefaultHttpClient;
// CloseableHttpClient;

public class DaPaasUserGateway extends HttpGateway {
  private static final Logger logger = Logger.getLogger(DaPaasUserGateway.class);
  private DaPaasParams        params = new DaPaasParams();
  private CloseableHttpClient client;
  private HttpClientContext   context;

  public DaPaasUserGateway(HttpMethod method, String url) {
    super(method, url);
    RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BEST_MATCH).build();
    CookieStore cookieStore = new BasicCookieStore();
    context = HttpClientContext.create();
    context.setCookieStore(cookieStore);
    client = HttpClients.custom().setDefaultRequestConfig(globalConfig).setDefaultCookieStore(cookieStore).build();
  }

  public DaPaasUserGateway(HttpMethod method, String url, DaPaasParams params) {
    this(method, url);
    this.params = params;
  }

  public void modifiedUserGateway(HttpMethod method, String url) {
    this.method = method;
    this.url = url;
  }

  public void modifiedUserGateway(HttpMethod method, String url, DaPaasParams params) {
    modifiedUserGateway(method, url);
    this.params = params;
  }

  @Override
  protected HttpResponse post() throws IOException {
    HttpPost httpPost = new HttpPost(url);
    logger.debug("jsonobj: " + params.getJsonObject());
    httpPost.addHeader("Content-Type", "application/json");
    for (String key : params.getHeaders().keySet()) {
      if (key.equals("Content-Type")) {
        httpPost.setHeader("Content-Type", params.getHeaders().get(key));
      } else {
        httpPost.addHeader(key, params.getHeaders().get(key));
      }
    }
    if (params.getJsonObject() != null) {
      httpPost.setEntity(new StringEntity(params.getJsonObject().getValue().toString()));
    }
    if (params.getFile() != null) {
      
//        if (params.getContentType() != null) {
//          httpPost.setHeader("Content-Type", params.getContentType());
//        }
        byte[] bytes = read(params.getFile().getValue());
        HttpEntity bentity = new ByteArrayEntity(bytes);
        httpPost.setEntity(bentity);
      
    }
    logger.debug("executing request " + httpPost.getRequestLine());
    HttpResponse response = client.execute(httpPost, context); /* , localContext */
    return response;
  }

  @Override
  protected HttpResponse get() throws IOException {
    HttpGet httpGet = new HttpGet(url);
    httpGet.addHeader("Accept", "application/json");
    for (String key : params.getHeaders().keySet()) {
      if (key.equals("Accept")) {
        httpGet.setHeader("Accept", params.getHeaders().get(key));
      } else {
        httpGet.addHeader(key, params.getHeaders().get(key));
      }
    }
    logger.debug("executing request " + httpGet.getRequestLine());
    HttpResponse response = client.execute(httpGet, context);
    return response;
  }

  @Override
  protected HttpResponse put() throws IOException {
    HttpPut httpPut = new HttpPut(url);
    logger.debug("jsonobj: " + params.getJsonObject());
    httpPut.addHeader("Content-Type", "application/json");
    for (String key : params.getHeaders().keySet()) {
      if (key.equals("Content-Type")) {
        httpPut.setHeader("Content-Type", params.getHeaders().get(key));
      } else {
        httpPut.addHeader(key, params.getHeaders().get(key));
      }
    }
    if (params.getJsonObject() != null) {
      StringEntity stringEntity = new StringEntity(params.getJsonObject().getValue().toString());
      httpPut.setEntity(stringEntity);
    }
    logger.debug("executing request " + httpPut.getRequestLine());
    HttpResponse response = client.execute(httpPut, context);
    context.getCookieStore().getCookies();
    // (InternalHttpClient) client.
    return response;
  }

  @Override
  protected HttpResponse delete() throws IOException {
    HttpDelete httpDelete = new HttpDelete(url);
    httpDelete.addHeader("Content-Type", "application/json");
    for (String key : params.getHeaders().keySet()) {
      httpDelete.addHeader(key, params.getHeaders().get(key));
    }
    logger.debug("executing request " + httpDelete.getRequestLine());
    HttpResponse response = client.execute(httpDelete, context);
    return response;
  }

  public void close() {
    try {
      client.close();
    } catch (Exception io) {
      logger.error(io);
    }
  }

  public HttpClientContext getContext() {
    return context;
  }

  public void setContext(HttpClientContext context) {
    this.context = context;
  }
}
