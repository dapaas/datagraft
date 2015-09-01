package eu.dapaas.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncService {
  private static final AsyncService INSTANCE = new AsyncService();
  
  public static AsyncService getInstance() {
    return INSTANCE;
  }
  
  private final ExecutorService pool;
  
  private AsyncService() {
    pool = Executors.newCachedThreadPool();
  }
  
  public void execute(AsyncJob job) {
    pool.execute(job);
  }
  
  public void shutdown() {
    pool.shutdown();
  }
  
  public void shutdownNow() {
    pool.shutdownNow();
  }
}
