package eu.dapaas.utils;

import org.apache.log4j.Logger;

public abstract class AsyncJob implements Runnable{
  private static final Logger logger = Logger.getLogger(AsyncJob.class);

  protected static final Integer MAX_ATTEMPTS = 10; //Integer.MAX_VALUE;

  @Override
  public void run() {
      try {
          for (int i = 0; i < MAX_ATTEMPTS; ++i) {
              if (execute())
                  break;
              Thread.sleep(5000);
          }
      } catch(Throwable e) {
          logger.fatal(e.getMessage(), e);
      } 
  }

  protected abstract boolean execute() throws Throwable;
}
