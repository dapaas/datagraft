package eu.dapaas.http.impl;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.EntityBuilder;
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
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import eu.dapaas.http.HttpGateway;
import eu.dapaas.http.HttpMethod;

public class DaPaasGateway extends HttpGateway {
  private static final Logger logger = Logger.getLogger(DaPaasGateway.class);
  private DaPaasParams        params = new DaPaasParams();
  private String              username;
  private String              password;
  private CloseableHttpClient client;
  private HttpClientContext   context;

  public DaPaasGateway(HttpMethod method, String username, String password, String url) {
    super(method, url);
    context = HttpClientContext.create();
    if (username != null && password != null) {
      UsernamePasswordCredentials c = new UsernamePasswordCredentials(username, password);
      BasicCredentialsProvider cp = new BasicCredentialsProvider();
      cp.setCredentials(AuthScope.ANY, c);
      context.setCredentialsProvider(cp);
      client = HttpClientBuilder.create().setDefaultCredentialsProvider(cp).build();
      this.username = username;
      this.password = password;
    } else {
      client = HttpClientBuilder.create().build();
    }

  }

  public DaPaasGateway(HttpMethod method, String username, String password, String url, DaPaasParams params) {
    this(method, username, password, url);
    this.params = params;
  }

  public void modifiedDaPaasGateway(HttpMethod method, String url) {
    this.method = method;
    this.url = url;

  }

  public void modifiedDaPaasGateway(HttpMethod method, String url, DaPaasParams params) {
    modifiedDaPaasGateway(method, url);
    this.params = params;
  }

  @Override
  protected HttpResponse post() throws IOException {
    HttpPost httpPost = new HttpPost(url);
    if (username != null && password != null){
      try{
        httpPost.addHeader(new BasicScheme().authenticate(new UsernamePasswordCredentials(username, password), httpPost, context));
      }catch(AuthenticationException e){
        logger.error("", e);
      }
    }
    logger.debug("jsonobj: " + params.getJsonObject());
    httpPost.addHeader("Content-Type", "application/ld+json");
    for (String key : params.getHeaders().keySet()) {
      if (key.equals("Content-Type")) {
        httpPost.setHeader("Content-Type", params.getHeaders().get(key));
      } else {
        httpPost.addHeader(key, params.getHeaders().get(key));
      }
    }
    if (params.getJsonObject() != null && !params.isMultipart()) {
      httpPost.setEntity(new StringEntity(params.getJsonObject().getValue().toString()));
    }
    if (params.isMultipart()) {
      // HttpEntity bab = null;
      StringBody sbFilename = null;
      StringBody sbMeta = null;
      if (params.getFilename() != null) {
        sbFilename = new StringBody(params.getFilename().getValue(), ContentType.TEXT_PLAIN.withCharset("UTF-8"));
      }
      if (params.getJsonObject() != null) {
        ContentType ct = ContentType.create("application/ld+json");
        sbMeta = new StringBody(params.getJsonObject().getValue().toString(), ct);
      }
      // ContentType entityContent = ContentType.create("multipart/mixed");
      if (params.getHeaders().containsKey("Content-Type")) {
        httpPost.setHeader("Content-Type", params.getHeaders().get("Content-Type"));

      } else {
        httpPost.setHeader("Content-Type", "multipart/form-data; boundary=&");
      }
      MultipartEntityBuilder entity = MultipartEntityBuilder.create();
      // entity.setContentType(entityContent);
      entity.setBoundary("&");
      if (sbFilename != null) {
        entity.addPart(params.getFilename().getName(), sbFilename);
      }
      if (params.getFile() != null) {
        entity.addBinaryBody(params.getFile().getName(), params.getFile().getValue());
      }
      if (sbMeta != null) {
//        entity.setMode(HttpMultipartMode.)
        entity.addPart(params.getJsonObject().getName(), sbMeta);
      }
      httpPost.setEntity(entity.build());
    } else {
      logger.debug("has file  : "+params.getFile());
      if (params.getFile() != null) {
        logger.debug("has file with name : "+params.getFile().getValue().getName());
        logger.debug("Start put file");
        byte[] bytes = read(params.getFile().getValue());
        EntityBuilder  bentity = EntityBuilder.create();
        bentity.setBinary(bytes);
        bentity.chunked();
        httpPost.setEntity(bentity.build());
        logger.debug("end put file");
      }
    }
    logger.debug("executing request " + httpPost.getRequestLine());
    HttpResponse response = client.execute(httpPost); /* , localContext */
    return response;
  }

  @Override
  protected HttpResponse get() throws IOException {
    HttpGet httpGet = new HttpGet(url);
    //String username, String password
    if (username != null && password != null){
      try{
      httpGet.addHeader(new BasicScheme().authenticate(new UsernamePasswordCredentials(username, password), httpGet, context));
      }catch(AuthenticationException e){
        logger.error("", e);
      }
    }
    httpGet.addHeader("Accept", "application/ld+json");
    for (String key : params.getHeaders().keySet()) {
      if (key.equals("Accept")) {
        httpGet.setHeader("Accept", params.getHeaders().get(key));
      } else {
        httpGet.addHeader(key, params.getHeaders().get(key));
      }
    }
    logger.debug("executing request " + httpGet.getRequestLine());
    HttpResponse response = client.execute(httpGet);
    return response;
  }

  @Override
  protected HttpResponse put() throws IOException {
    HttpPut httpPut = new HttpPut(url);
    logger.debug("jsonobj: " + params.getJsonObject());
    httpPut.addHeader("Content-Type", "application/ld+json");
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
    HttpResponse response = client.execute(httpPut);
    return response;
  }

  @Override
  protected HttpResponse delete() throws IOException {
    HttpDelete httpDelete = new HttpDelete(url);
    httpDelete.addHeader("Content-Type", "application/ld+json");
    for (String key : params.getHeaders().keySet()) {
      httpDelete.addHeader(key, params.getHeaders().get(key));
    }
    logger.debug("executing request " + httpDelete.getRequestLine());
    HttpResponse response = client.execute(httpDelete);
    return response;
  }
}
