package model;

import config.SenderPropertiesConfig;

import java.util.Map;


public class Sender {

    private final String emailAddress;
    private final String password;

    public Sender() {
        Map<String, String> senderDetails = SenderPropertiesConfig.readConfig();
        emailAddress = senderDetails.get("SENDER_ADDRESS");
        password = senderDetails.get("SENDER_PWD");
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }
}
