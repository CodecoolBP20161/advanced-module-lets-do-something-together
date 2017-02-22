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

    public void send(Email email) {
        Sender sender = email.getSender();
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender.getEmailAddress(), sender.getPassword());
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender.getEmailAddress()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getReceiver()));
            message.setSubject(email.getSubject());
            message.setContent(email.getBody(), "text/html");
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}

