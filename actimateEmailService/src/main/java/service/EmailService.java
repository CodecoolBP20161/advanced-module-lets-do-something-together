package service;


import model.Email;
import model.Sender;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private static EmailService INSTANCE = null;

    private EmailService() {
    }

    public static EmailService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EmailService();
        }
        return INSTANCE;
    }

    private Properties setupProps() {
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        return props;
    }

    private MimeMessage setupMimeMessage(Email email, Session session, Sender sender) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender.getEmailAddress()));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getReceiver()));
        message.setSubject(email.getSubject());
        message.setContent(email.getBody(), "text/html");
        return message;
    }

    public boolean send(Email email) {
        Sender sender = email.getSender();

        Session session = Session.getInstance(setupProps(), new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender.getEmailAddress(), sender.getPassword());
            }
        });

        try {
            MimeMessage message = setupMimeMessage(email, session, sender);
            Transport.send(message);
            return true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
    }
}

