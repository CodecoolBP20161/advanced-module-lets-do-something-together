package com.codecool.email;

import com.codecool.model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ContactEmailHandler extends AbstractEmailHandler {

    private static final Logger logger = LoggerFactory.getLogger(ContactEmailHandler.class);

    private final String contactSubject = "New contact from website";

    String getContactSubject() {
        return contactSubject;
    }

    List<Contact> getNewContacts() {
        logger.info("New users without sent welcome email collected to list.");
        return contactRepository
                .findAllByForwarded(false);
    }

    void updateContactEmailStatus(String email) {
        String contactEmail = email.substring(email.indexOf("+") + 1, email.indexOf("@"));
        contactRepository.findAllByForwarded(false)
                .stream()
                .filter(contact ->
                        replaceNonAlphaNumericCharacters(contact.getEmail()).equals(contactEmail))
                .forEach(contact -> contact.setForwarded(true));
        logger.info("Contact email status set to forwarded for new contact(s).");
    }

    String concatEmailAddress(Contact contact) {
        logger.info("Contact email address concatenated with app email.");
        return String.format("actimate.app+%s@gmail.com", replaceNonAlphaNumericCharacters(contact.getEmail()));
    }

    private String replaceNonAlphaNumericCharacters(String email) {
        logger.info("Email address special charcters replaced in '{}'.", email);
        return email.replaceAll("\\W", "");
    }

    String formatContactEmail(Contact contact) {
        logger.info("Creating contact email template from contact data from '{}'.", contact.getName());
        return String.format("<b>New contact</b><br><br>" +
                        "A visitor from the Actimate app called <b>%s</b> has contacted us at %s.<br>" +
                        "The message is the following:<br>" +
                        "<blockquote><i>%s</i></blockquote>" +
                        "Send your answer to the visitor's email address: %s<br>" +
                        "Have a nice day!<br><br><br>" +
                        "<i>This is a generated message, do not reply to it.</i>",
                contact.getName(), contact.getDate(), contact.getMessage(), contact.getEmail());
    }
}
