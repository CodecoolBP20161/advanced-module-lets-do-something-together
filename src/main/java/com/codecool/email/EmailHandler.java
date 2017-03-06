package com.codecool.email;

import com.codecool.model.User;
import com.codecool.model.UserEmail;
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
}
