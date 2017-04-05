package com.codecool.controller;

import com.codecool.email.EmailHandler;
import com.codecool.model.Contact;
import com.codecool.model.User;
import com.codecool.repository.ContactRepository;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class AppEmailController {
    private static final Logger logger = LoggerFactory.getLogger(AppEmailController.class);

    private static final String URL = "http://localhost:60227";
    @Autowired
    private EmailHandler emailHandler;
    private final String emailSubject = "Welcome to ActiMate";
    private String contactSubject = "New contact from website";

    @Autowired
    public ContactRepository contactRepository;

    public static String builderGet() {
        logger.info("Get list of email addresses from the microservice where email status is 'sent'.");
        URIBuilder builder;
        String result = "";
        try {
            builder = new URIBuilder(URL + "/sent");
            result = execute(builder.build());
            logger.info("Successful execution of GET request. Route: '/sent'");
        } catch (IOException | URISyntaxException e) {
            logger.error("{} occurred while executing the GET request: {}.", e.getCause(), e.getMessage());
        }
        return result;
    }

    private static String execute(URI uri) throws IOException, URISyntaxException {
        logger.info("Execute method called on request. Uri: {}", uri);
        return Request.Get(uri).execute().returnContent().asString();
    }

    private String createJson(List<String> users, String template, String subject) {
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

    private void postJson(String jsonString) {
        logger.info("POST json to the microservice.");
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(URL);
        StringEntity params;
        try {
            params = new StringEntity(jsonString);
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);
            httpClient.execute(request);
            logger.info("Successful execution of POST request. Route: '/'");
        } catch (IOException e) {
            logger.error("{} occurred while executing the POST request: {}.", e.getCause(), e.getMessage());
        }
    }

    @Scheduled(fixedDelayString = "29000")
    private void manageNewRegistrations() {
        logger.info("Checking db for new registrations at {}", new Date());
        List<String> emails = emailHandler
                .checkEmailStatus()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        if (emails.size() > 0) {
            logger.info("Forwarding new registrations to the microservice at {}", new Date());
            postJson(createJson(emails, getWelcomeEmailTemplate(), emailSubject));
        }
    }

    @Scheduled(fixedDelayString = "31000")
    private void manageSentEmails() {
        logger.info("Checking microservice for sent emails at {}", new Date());
        String response = builderGet();
        if (response.length() > 0) {
            List<String> sentEmails = Arrays.asList(response.split(","));
            logger.info("{} emails sent, statuses to be updated.", sentEmails.size());
            emailHandler.updateEmailStatus(sentEmails);
        }
    }

    private String getWelcomeEmailTemplate() {
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


    @Scheduled(fixedDelayString = "300000")
    private void manageNewContacts() {
        logger.info("Checking new contacts in the db.");
        List<Contact> unforwardedContacts = contactRepository.findAllByForwarded(false);
        if (unforwardedContacts.size() > 0) {
            logger.info("{} new contacts present.", unforwardedContacts.size());
            unforwardedContacts.forEach(contact ->
                    postJson(
                            createJson(
                                    Collections.singletonList(emailHandler.concatEmailAddress(contact)),
                                    formatContactEmail(contact),
                                    contactSubject)));
            logger.info("Sending new contact details to the microservice");
        }
    }

    private String formatContactEmail(Contact contact) {
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