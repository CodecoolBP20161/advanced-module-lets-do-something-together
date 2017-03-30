package com.codecool.email;

import com.codecool.model.Contact;
import com.codecool.model.User;
import com.codecool.model.UserEmail;
import com.codecool.repository.ContactRepository;
import com.codecool.repository.UserEmailRepository;
import com.codecool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmailHandler {

    @Autowired
    private UserEmailRepository userEmailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public ContactRepository contactRepository;

    public List<User> checkEmailStatus() {
        return userEmailRepository
                .findAllByEmailSent(false)
                .stream()
                .map(UserEmail::getUser)
                .collect(Collectors.toList());
    }

    public void updateEmailStatus(List<String> emails) {
        for (String email : emails) {
            try {
                UserEmail userEmail = userEmailRepository.findByUser(userRepository.findByEmail(email));
                userEmail.setEmailSent(true);
                userEmailRepository.save(userEmail);
            } catch (NullPointerException e) {
                e.getMessage();
                manageContactEmails(email);
            }
        }
    }

    private void manageContactEmails(String email) {
        String contactEmail = email.substring(email.indexOf("+") + 1, email.indexOf("@"));
        contactRepository.findAllByForwarded(false)
                .stream()
                .filter(contact ->
                        replaceNonAlphaNumericCharacters(contact.getEmail()).equals(contactEmail))
                .forEach(contact -> contact.setForwarded(true));
    }


    public String concatEmailAddress(Contact contact) {
        return String.format("actimate.app+%s@gmail.com", replaceNonAlphaNumericCharacters(contact.getEmail()));
    }

    private String replaceNonAlphaNumericCharacters(String email) {
        return email.replaceAll("\\W", "");
    }
}
