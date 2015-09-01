package eu.dapaas.notification.impl;

import java.io.IOException;
import java.util.Calendar;

import com.sirmamobile.encrypt.Base64;

import eu.dapaas.notification.Notification;
import eu.dapaas.utils.Config;
import eu.dapaas.utils.ResourceBundleUtil;
import eu.dapaas.utils.Utils;

public class EmailVerificationMessage extends Notification {

  private String email;
  private String username;
  private String name;
  
  public EmailVerificationMessage(String email, String username, String name) {
    this.email = email;
    this.username = username;
    this.name = name;
    
  }
  
  @Override
  public String getContent() throws IOException {
    // email+;+datelong+;+username
    String decodelink = email + ";" + Calendar.getInstance().getTime().getTime() + ";" + username + ";";
    String encodelink = Base64.encodeBytes(decodelink.getBytes());
    Object[] arguments = new Object[3];
    if (Utils.isEmpty(name)) {
      arguments[0] = email.substring(0, email.indexOf("@"));
    } else {
      arguments[0] = name;
    }
    arguments[1] = encodelink;
    arguments[2] = Config.getInstance().getAplicationUrl()+"/confirm";
    String text = ResourceBundleUtil.getMailContent("verification.email", arguments);
    return text;
  }

  @Override
  public String getTitle() {
    return "DATAGRAFT: email confirmation";
  }
}
