package com.codecool.email;

import com.codecool.model.Contact;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmailHandler extends AbstractEmailHandler {

    private static final Logger logger = LoggerFactory.getLogger(EmailHandler.class);

    @Autowired
    private WelcomeEmailHandler welcomeEmailHandler;

    @Autowired
    private ContactEmailHandler contactEmailHandler;

    private String actimateEmail = "actimate.app";

    public void manageSentEmails(String response) {
        logger.info("Checking microservice for sent emails at {}", new Date());
        if (response.length() > 0) {
            List<String> sentEmails = Arrays.asList(response.split(","));
            logger.info("{} emails sent, statuses to be updated.", sentEmails.size());
            updateEmailStatus(sentEmails);
        }
    }

    private void updateEmailStatus(List<String> emails) {
        logger.info("{} user(s)' email(s)' status(es) updated.", emails.size());
        for (String email : emails) {
            if (email.toLowerCase().contains(actimateEmail)) {
                contactEmailHandler.updateContactEmailStatus(email);
            } else {
                welcomeEmailHandler.updateWelcomeEmailStatus(email);
            }
        }
    }

    public String manageWelcomeEmails() {
        logger.info("Checking db for new registrations at {}", new Date());
        String payload = null;
        List<String> emails = welcomeEmailHandler.getNewRegistrations();
        if (emails.size() > 0) {
            logger.info("{} new registrations present.", emails.size());
            payload = createEmailJson(emails, welcomeEmailHandler.getWelcomeEmailTemplate(), welcomeEmailHandler.getWelcomeSubject());
        }
        return payload;
    }

    public List<String> manageContactEmails() {
        logger.info("Checking new contacts in the db.");
        List<String> payload = null;
        List<Contact> unforwardedContacts = contactEmailHandler.getNewContacts();
        if (unforwardedContacts.size() > 0) {
            logger.info("{} new contacts present.", unforwardedContacts.size());
            payload = unforwardedContacts.stream().map(contact ->
                    createEmailJson(
                            Collections.singletonList(contactEmailHandler.concatEmailAddress(contact)),
                            contactEmailHandler.formatContactEmail(contact),
                            contactEmailHandler.getContactSubject())).collect(Collectors.toList());
            logger.info("Sending new contact details to the microservice");
        }
        return payload;
    }

    private String createEmailJson(List<String> users, String template, String subject) {
        logger.info("Create json of prepared emails for the microservice.");
        String jsonString = "";
        try {
            jsonString = new JSONObject().put("emails", String.join(",", users)).put("template", template).put("subject", subject).toString();
            logger.info("Email json successfully created.");
        } catch (JSONException e) {
            logger.error("{} occurred while creating emails json.", e.getCause(), e.getMessage());
        }
        return jsonString;
    }
}
