package com.codecool.email;


import com.codecool.email.model.WelcomeEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
class WelcomeEmailHandler extends AbstractEmailHandler {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeEmailHandler.class);

    private final String welcomeSubject = "Welcome to ActiMate";

    String getWelcomeSubject() {
        return welcomeSubject;
    }

    List<String> getNewRegistrations() {
        logger.info("New users without sent welcome email collected to list.");
        return welcomeEmailRepository
                .findAllByEmailSent(false)
                .stream()
                .map((welcomeEmail) -> welcomeEmail.getUser().getEmail())
                .collect(Collectors.toList());
    }

    void updateWelcomeEmailStatus(String email) {
        WelcomeEmail welcomeEmail = welcomeEmailRepository.findByUser(userRepository.findByEmail(email));
        welcomeEmail.setEmailSent(true);
        welcomeEmailRepository.save(welcomeEmail);
        logger.info("Welcome email status set to sent for new user.");
    }

    String getWelcomeEmailTemplate() {
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
}
