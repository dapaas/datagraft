package eu.dapaas.notification;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import eu.dapaas.utils.AsyncJob;
import eu.dapaas.utils.AsyncService;


public class EmailNotificationService {
  private static final Logger logger = Logger.getLogger(EmailNotificationService.class);
  private static final String EMAIL_PATTERN = "^^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-\\+]+)*@[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
  
  private static final EmailNotificationService INSTANCE = new EmailNotificationService();

  public static EmailNotificationService getInstance() {
      return INSTANCE;
  }

  protected EmailNotificationService() {

  }

  public void sendNotification(Notification message, String receiver)  {
      sendMessage(message, receiver);
  }

  public void sendNotificationAsync(final Notification message, final String receiver)  {
      AsyncService.getInstance().execute(new AsyncJob() {
          @Override
          protected boolean execute() throws Throwable {
              sendMessage(message, receiver);
              return true;
          }
      });
  }
  
  

  public void sendMessage(Notification message, String receiver)  {
   
    try {
      logger.info("Request to send message to " + receiver + ":\n" + message.getContent());
      MimeMessage mimeMessage = createMimeMessage(message.getTitle(), message.getContent(), receiver);
      Outbox.getInstance().sendMessage(mimeMessage);
    } catch (MessagingException e) {
      e.printStackTrace();
      logger.error("Can not send email", e);
    }catch(IOException e){
      e.printStackTrace();
      logger.error("Can not send email", e);
    }
  }


  public boolean accept(String receiver) {
    return receiver != null && receiver.matches(EMAIL_PATTERN);
  }


  public String getFallbackMessage(String receiver) {
    return "Sorry, this is not a valid email address";
  }

  private MimeMessage createMimeMessage(String subject, String message, String receiver) throws MessagingException, IOException {
    MimeMessage msg = new MimeMessage(Outbox.getInstance().getSession());
    msg.setSubject(subject, Outbox.ENCODING);
    msg.setSender(new InternetAddress(Outbox.MAIL_SENDER));
    msg.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiver));
    msg.setHeader("Content-Type", Outbox.CONTENT_TYPE);
    msg.setContent(message, Outbox.CONTENT_TYPE);
    return msg;
  }

}
