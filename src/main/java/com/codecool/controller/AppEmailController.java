package com.codecool.controller;

import com.codecool.email.EmailHandler;
import com.codecool.model.User;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class AppEmailController {

    private static final String URL = "http://localhost:60227";

    @Autowired
    private static EmailHandler emailHandler;

    @Autowired
    public AppEmailController(EmailHandler handler) {
        emailHandler = handler;
    }

    public static void builderSend(String users) {
        URIBuilder builder;
        try {
            builder = new URIBuilder(URL);
            builder.addParameter("emails", users);
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

    @Scheduled(fixedDelayString = "30000")
    private void manageNewRegistrations() {
        List<String> emails = emailHandler
                .checkEmailStatus()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        builderSend(String.join(",", emails));
    }

    @Scheduled(fixedDelayString = "30001")
    private void manageSentEmails() {
        List<String> sentEmails = Arrays.asList(builderGet().split(","));
        if (sentEmails.size() > 1) {
            emailHandler.updateEmailStatus(sentEmails);
        }
    }
}
