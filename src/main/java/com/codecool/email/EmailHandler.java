package com.codecool.email;

import com.codecool.model.User;
import com.codecool.model.UserEmail;
import com.codecool.repository.UserEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailHandler {

    @Autowired
    private UserEmailRepository userEmailRepository;

    public List<User> checkEmailStatus() {
        return userEmailRepository.findAllByEmailSent(false).stream().map(UserEmail::getUser).collect(Collectors.toList());
    }
}
