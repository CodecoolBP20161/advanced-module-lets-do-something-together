package com.codecool.util;

import com.codecool.model.User;
import com.codecool.model.UserEmail;
import com.codecool.repository.UserEmailRepository;
import com.codecool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmailHandler {

    @Autowired
    private UserEmailRepository userEmailRepository;

    @Autowired
    private UserRepository userRepository;


    public List<User> checkEmailStatus() {
        return userEmailRepository
                .findAllByEmailSent(false)
                .stream()
                .map(UserEmail::getUser)
                .collect(Collectors.toList());
    }

    public void updateEmailStatus(List<String> emails) {
        for (String email : emails) {
            UserEmail userEmail = userEmailRepository.findByUser(userRepository.findByEmail(email));
            userEmail.setEmailSent(true);
            userEmailRepository.save(userEmail);
        }
    }

    public String getWelcomeEmailTemplate() {
        StringWriter writer = new StringWriter();
        try {
            spark.utils.IOUtils.copy(new FileInputStream(new File("./src/main/resources/templates/email_templates/admin_email.html")), writer);
        } catch (IOException e) {
            e.getMessage();
        }
        return writer.toString();
    }
}
