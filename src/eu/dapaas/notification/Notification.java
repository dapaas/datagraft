package eu.dapaas.notification;

import java.io.IOException;

public abstract class Notification {
  public static final String ENCODING = "UTF-8";
  
  public Notification() {
   
  }

  public abstract String getContent() throws IOException;

  public abstract String getTitle();
}
