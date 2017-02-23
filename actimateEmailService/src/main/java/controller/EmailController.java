package controller;


import dao.EmailAddressDao;
import dao.impl.EmailAddressDaoImpl;
import model.Email;
import model.Sender;
import service.EmailService;
import spark.Request;
import spark.Response;
import spark.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EmailController {
    private static EmailAddressDao emailAddressDao = EmailAddressDaoImpl.getInstance();
    private final EmailService service;
    private final String emailSubject = "Welcome to ActiMate";

    public EmailController(EmailService service) {
        this.service = service;
    }

    private void saveAdresses(Request req) {
        String emailString = req.queryParams("emails");
        emailAddressDao.save(new ArrayList<>(
                Arrays.asList(emailString.split(","))));
    }

    public Response sendToAddresses(Request req, Response res) {
        saveAdresses(req);
        List<String> addresses = emailAddressDao.getAllNew();
        if (addresses != null) {
            addresses.stream()
                    .filter(address ->
                            service.send(createEmail(address)))
                    .forEach(address ->
                            emailAddressDao.switchSentStatus(address));
        }
        return res;
    }

    public String sentAddresses(Request req, Response res) {
        List<String> addresses = new ArrayList<>();
        for (String address : emailAddressDao.getAllSent()) {
            addresses.add(address);
            emailAddressDao.removeSent();
        }
        return String.join(",", addresses);
    }

    private Email createEmail(String recipient) {
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(new FileInputStream(new File("./src/main/resources/templates/email.html")), writer);
        } catch (IOException e) {
            e.getMessage();
        }
        return new Email(new Sender(), recipient, emailSubject, writer.toString());
    }
}