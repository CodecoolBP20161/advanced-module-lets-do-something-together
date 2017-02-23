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

    public Response saveAdresses(Request req, Response res) {
        String emailString = req.queryParams("emails");
        List<String> users = new ArrayList<>(Arrays.asList(emailString.split(",")));
        emailAddressDao.save(users);
        return res;
    }

    public void sendToAddresses() {
        List<String> addresses = emailAddressDao.getAll();
        if (addresses != null) {
            addresses.stream().filter(address -> service.send(createEmail(address))).forEach(address -> {
                emailAddressDao.switchSentStatus(address);
                emailAddressDao.removeSent();
            });
        }
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