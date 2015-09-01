package eu.dapaas.notification;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import eu.dapaas.utils.Config;

public class Outbox {
  public static final String  ENCODING     = Notification.ENCODING;
  public static final String  CONTENT_TYPE = "text/html; charset=" + ENCODING;
  public static final String  MAIL_SENDER  = Config.getInstance().getMailSender();
  
  private static final Logger logger       = Logger.getLogger(Outbox.class);
  
  private static Outbox       instance     = new Outbox();
  
  public static Outbox getInstance() {
    return instance;
  }
  
  private Session session;
  
  private Outbox() {
    String host = Config.getInstance().getMailSmtpHost();
    String auth = Config.getInstance().getMailSmtpAuth();
    
    logger.info("start initializing mail session");
    logger.info("smtp host is " + host);
    logger.info("smtp authentication: " + auth);

    Properties props = new Properties();
    props.put("mail.smtp.auth", auth);
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", "587");
  
    session = Session.getInstance(props, new EmailAuthenticator());
  }
  
  public Session getSession() {
    return session;
  }
  
  public void sendMessage(MimeMessage msg) throws MessagingException, IOException {
    if (msg == null)
      return;
    Address[] to = msg.getAllRecipients();
    if (to == null || to.length == 0) {
      logger.info("no recipients found");
      return;
    }
    
    StringBuilder addressees = new StringBuilder();
    for (Address next : to) {
      addressees.append(next.toString()).append(" ");
      logger.info("message should be sent to " + next.toString());
    }
    
    
    
    logger.info("message sent");
    Transport.send(msg);
  }
}
