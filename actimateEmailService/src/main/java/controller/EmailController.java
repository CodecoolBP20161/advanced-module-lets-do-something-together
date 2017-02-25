package controller;


import dao.EmailAddressDao;
import dao.impl.EmailAddressDaoImpl;
import model.Email;
import model.Sender;
import service.EmailService;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EmailController {
    private static EmailAddressDao emailAddressDao = EmailAddressDaoImpl.getInstance();
    private final EmailService service;

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
                            service.send(createEmail(address, req)))
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

    private Email createEmail(String recipient, Request req) {
        return new Email(new Sender(), recipient, req.queryParams("subject"), req.queryParams("template"));
    }
}