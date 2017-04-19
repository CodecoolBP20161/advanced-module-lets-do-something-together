package com.codecool.email;


import com.codecool.email.repository.WelcomeEmailRepository;
import com.codecool.repository.ContactRepository;
import com.codecool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
abstract class AbstractEmailHandler {

    @Autowired
    WelcomeEmailRepository welcomeEmailRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactRepository contactRepository;
}
