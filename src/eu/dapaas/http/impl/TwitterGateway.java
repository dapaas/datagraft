package eu.dapaas.http.impl;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import eu.dapaas.http.HttpGateway;
import eu.dapaas.http.HttpMethod;

public class TwitterGateway extends HttpGateway {
  private static final Logger logger = Logger.getLogger(FacebookGateway.class);
  private CloseableHttpClient client;
  
  public TwitterGateway(HttpMethod method, String url) {
    super(method, url);
    client = HttpClientBuilder.create().build();
  }

  @Override
  protected HttpResponse post() throws IOException {
    HttpPost postRequest = new HttpPost(url);
    HttpResponse response = client.execute(postRequest);
    int status = response.getStatusLine().getStatusCode();
    logger.info("returned " + status);
    if (status != HttpStatus.SC_OK)
      throw new IOException(status + ": " + response.getStatusLine().getReasonPhrase());

    return response;
  }

  @Override
  protected HttpResponse get() throws IOException {
    HttpGet getRequest = new HttpGet(url);
    HttpResponse response = client.execute(getRequest);
    int status = response.getStatusLine().getStatusCode();
    logger.info("returned " + status);
    if (status != HttpStatus.SC_OK)
      throw new IOException(status + ": " + response.getStatusLine().getReasonPhrase());

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
