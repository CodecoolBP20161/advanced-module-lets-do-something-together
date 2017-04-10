package com.codecool.util;

import com.codecool.model.Contact;
import com.codecool.model.User;
import com.codecool.model.UserEmail;
import com.codecool.repository.ContactRepository;
import com.codecool.repository.UserEmailRepository;
import com.codecool.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmailHandler {
    private static final Logger logger = LoggerFactory.getLogger(EmailHandler.class);

    @Autowired
    private UserEmailRepository userEmailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public ContactRepository contactRepository;

    public List<User> checkEmailStatus() {
        logger.info("New users without welcome email collected to list.");
        return userEmailRepository
                .findAllByEmailSent(false)
                .stream()
                .map(UserEmail::getUser)
                .collect(Collectors.toList());
    }

    public void updateEmailStatus(List<String> emails) {
        logger.info("{} user(s)' email(s)' status(es) updated.", emails.size());
        for (String email : emails) {
            try {
                UserEmail userEmail = userEmailRepository.findByUser(userRepository.findByEmail(email));
                userEmail.setEmailSent(true);
                userEmailRepository.save(userEmail);
                logger.info("Welcome email status set to sent for new user.");
            } catch (NullPointerException e) {
                e.getMessage();
                logger.info("Contact email status set to forwarded for new contact.");
                manageContactEmails(email);
            }
        }
    }

    private void manageContactEmails(String email) {
        String contactEmail = email.substring(email.indexOf("+") + 1, email.indexOf("@"));
        logger.info("Update contact email status for '{}'.", contactEmail);
        contactRepository.findAllByForwarded(false)
                .stream()
                .filter(contact ->
                        replaceNonAlphaNumericCharacters(contact.getEmail()).equals(contactEmail))
                .forEach(contact -> contact.setForwarded(true));
    }


    public String concatEmailAddress(Contact contact) {
        logger.info("Contact email address concatenated with app email.");
        return String.format("actimate.app+%s@gmail.com", replaceNonAlphaNumericCharacters(contact.getEmail()));
    }

    private String replaceNonAlphaNumericCharacters(String email) {
        logger.info("Email address special charcters replaced in '{}'.", email);
        return email.replaceAll("\\W", "");
    }

    public String getWelcomeEmailTemplate() {
        logger.info("Getting welcome email template.");
        StringWriter writer = new StringWriter();
        try {
            spark.utils.IOUtils.copy(new FileInputStream(new File("./src/main/resources/templates/email_templates/welcome_email.html")), writer);
            logger.info("Template successfully read.");
        } catch (IOException e) {
            logger.error("{} occurred while reading the welcome email template: {}.", e.getCause(), e.getMessage());
        }
        return writer.toString();
    }
}
