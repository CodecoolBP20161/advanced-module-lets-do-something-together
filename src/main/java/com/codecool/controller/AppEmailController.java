package com.codecool.controller;

import com.codecool.model.User;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class AppEmailController {

    private static final String URL = "http://localhost:60227";

    public static void builderSend(List<User> users) {
        URIBuilder builder;
        try {
            builder = new URIBuilder(URL);
            List<String> emails = users.stream().map(User::getEmail).collect(Collectors.toList());
            builder.addParameter("emails", String.join(",", emails));
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
}
