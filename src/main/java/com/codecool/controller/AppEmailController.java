package com.codecool.controller;

import com.codecool.email.EmailHandler;
import com.codecool.model.User;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
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
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class AppEmailController {

    private static final String URL = "http://localhost:60227";
    private final String emailSubject = "Welcome to ActiMate";

    @Autowired
    private static EmailHandler emailHandler;

    @Autowired
    public AppEmailController(EmailHandler handler) {
        emailHandler = handler;
    }

    public static void builderSend(String users, String template, String subject) {
        URIBuilder builder;
        try {
            builder = new URIBuilder(URL);
            builder.addParameter("emails", users);
            builder.addParameter("template", template);
            builder.addParameter("subject", subject);
            execute(builder.build());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

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

    @Scheduled(fixedDelayString = "30001")
    private void manageNewRegistrations() {
        List<String> emails = emailHandler
                .checkEmailStatus()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        if (emails.size() > 0) {
            builderSend(String.join(",", emails), getWelcomeEmailTemplate(), emailSubject);
        }
    }

    @Scheduled(fixedDelayString = "30000")
    private void manageSentEmails() {
        List<String> sentEmails = Arrays.asList(builderGet().split(","));
        if (sentEmails.size() > 1) {
            emailHandler.updateEmailStatus(sentEmails);
        }
    }

    private String getWelcomeEmailTemplate() {
        StringWriter writer = new StringWriter();
        try {
            spark.utils.IOUtils.copy(new FileInputStream(new File("./src/main/resources/templates/email_templates/email.html")), writer);
        } catch (IOException e) {
            e.getMessage();
        }
        return writer.toString();
    }
}
