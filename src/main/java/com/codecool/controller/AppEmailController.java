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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class AppEmailController {

    private static final String URL = "http://localhost:60227";
    @Autowired
    private static EmailHandler emailHandler;
    private final String emailSubject = "Welcome to ActiMate";
    private String appEmail = "actimate.app@gmail.com";
    private String contactSubject = "New contact from website";

    @Autowired
    public AppEmailController(EmailHandler handler) {
        emailHandler = handler;
    }

    @Autowired
    public ContactRepository contactRepository;

    public static String builderGet() {
        URIBuilder builder;
        String result = "";
        try {
            builder = new URIBuilder(URL + "/sent");
            result = execute(builder.build());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String execute(URI uri) throws IOException, URISyntaxException {
        return Request.Get(uri).execute().returnContent().asString();
    }

    private String createJson(List<String> users, String template, String subject) {
        String jsonString = "";
        try {
            jsonString = new JSONObject().put("emails", String.join(",", users)).put("template", template).put("subject", subject).toString();
        } catch (JSONException e) {
            e.getMessage();
        }
        return jsonString;
    }

    private void postJson(String jsonString) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(URL);
        StringEntity params;
        try {
            params = new StringEntity(jsonString);
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);
            httpClient.execute(request);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    @Scheduled(fixedDelayString = "29000")
    private void manageNewRegistrations() {
        List<String> emails = emailHandler
                .checkEmailStatus()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        if (emails.size() > 0) {
            postJson(createJson(emails, getWelcomeEmailTemplate(), emailSubject));
        }
    }

    @Scheduled(fixedDelayString = "31000")
    private void manageSentEmails() {
        String response = builderGet();
        List<String> sentEmails = new ArrayList<>();
        if (response.length() > 0) {
            sentEmails = Arrays.asList(response.split(","));
        }
        emailHandler.updateEmailStatus(sentEmails);
    }

    private String getWelcomeEmailTemplate() {
        StringWriter writer = new StringWriter();
        try {
            spark.utils.IOUtils.copy(new FileInputStream(new File("./src/main/resources/templates/email_templates/admin_email.html")), writer);
        } catch (IOException e) {
            e.getMessage();
        }
        return writer.toString();
    }


    @Scheduled(fixedDelayString = "300000")
    private void manageNewContacts() {
        List<Contact> unforwardedContacts = contactRepository.findAllByForwarded(false);
        if (unforwardedContacts.size() > 0) {
            for (Contact contact : unforwardedContacts) {
                postJson(createJson(Collections.singletonList(appEmail), formatContactEmail(contact), contactSubject));
            }
        }
    }

    private String formatContactEmail(Contact contact) {
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
