package eu.dapaas.notification;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import eu.dapaas.utils.Config;

public class EmailAuthenticator extends Authenticator {
  public EmailAuthenticator() {

  }

  @Override
  public PasswordAuthentication getPasswordAuthentication() {
      String username = Config.getInstance().getMailAuthUsername();
      String password = Config.getInstance().getMailAuthPassword();
      return new PasswordAuthentication(username, password);
  }
}
