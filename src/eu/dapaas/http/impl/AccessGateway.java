package eu.dapaas.http.impl;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import eu.dapaas.http.HttpGateway;
import eu.dapaas.http.HttpMethod;

public class AccessGateway extends HttpGateway {
  
  private static final Logger logger = Logger.getLogger(AccessGateway.class);
  private DaPaasParams        params = new DaPaasParams();
  private CloseableHttpClient client;
  private HttpClientContext   context;
  private String              username;
  private String              password;
  
  public AccessGateway(HttpMethod method, String username, String password, String url ) {
    super(method, url);
    context = HttpClientContext.create();
    UsernamePasswordCredentials c = new UsernamePasswordCredentials(username, password);
    BasicCredentialsProvider cp = new BasicCredentialsProvider();
    cp.setCredentials(AuthScope.ANY, c);
    context.setCredentialsProvider(cp);
    client = HttpClientBuilder.create().setDefaultCredentialsProvider(cp).build();
    this.username = username;
    this.password = password;
  }
  
  public AccessGateway(HttpMethod method, String url ) {
    super(method, url);
    client = HttpClientBuilder.create().build();
  }
  
  public AccessGateway(HttpMethod method, String username, String password, String url, DaPaasParams params) {
    this(method, username, password, url);
    this.params = params;
  }
  @Override
  protected HttpResponse post() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected HttpResponse get() throws IOException {
    HttpGet httpGet = new HttpGet(url);
    httpGet.addHeader("Accept", "application/json");
    for (String key : params.getHeaders().keySet()) {
      if (key.equals("Accept")){
        httpGet.setHeader(key, params.getHeaders().get(key));
      }else{
        httpGet.addHeader(key, params.getHeaders().get(key));
      }
    }
    if (username != null && password != null){
      try{
      httpGet.addHeader(new BasicScheme().authenticate(new UsernamePasswordCredentials(username, password), httpGet, context));
      }catch(AuthenticationException e){
        logger.error("", e);
      }
    }
    logger.debug("executing request " + httpGet.getRequestLine());
    HttpResponse response = client.execute(httpGet);
    
    return response;
  }

  @Override
  protected HttpResponse put() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected HttpResponse delete() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

}
