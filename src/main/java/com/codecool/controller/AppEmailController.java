package com.codecool.controller;

import com.codecool.email.EmailHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;


@Controller
public class AppEmailController {

    private static final Logger logger = LoggerFactory.getLogger(AppEmailController.class);
    private final String URL = "http://localhost:60227";
    @Autowired
    private EmailHandler emailHandler;

    @Scheduled(fixedDelayString = "31000")
    private void manageSentEmails() {
        String response = getSentEmails();
        if (response.length() > 0) {
            emailHandler.manageSentEmails(response);
        }
    }

    @Scheduled(fixedDelayString = "29000")
    private void manageNewRegistrations() {
        logger.info("Forwarding new registrations to the microservice at {}", new Date());
        postEmailJson(emailHandler.manageWelcomeEmails());
    }

    @Scheduled(fixedDelayString = "300000")
    private void manageNewContacts() {
        logger.info("Sending new contact details to the microservice");
        if (emailHandler.manageContactEmails() != null) {
            emailHandler.manageContactEmails().forEach(this::postEmailJson);
        }
    }

    private String getSentEmails() {
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

    private void postEmailJson(String jsonString) {
        logger.info("POST json to the microservice.");
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(URL);
        StringEntity params;
        if (jsonString != null) {
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
    }

    private String execute(URI uri) throws IOException, URISyntaxException {
        logger.info("Execute method called on request. Uri: {}", uri);
        return Request.Get(uri).execute().returnContent().asString();
    }

}