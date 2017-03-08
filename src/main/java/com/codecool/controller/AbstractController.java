package com.codecool.controller;


import com.codecool.model.User;
import com.codecool.model.UserDetail;
import com.codecool.repository.InterestRepository;
import com.codecool.repository.UserDetailRepository;
import com.codecool.security.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;

abstract class AbstractController {

    @Autowired
    UserDetailRepository userDetailRepository;

    @Autowired
    InterestRepository interestRepository;

    @Autowired
    UserService userService;

    UserDetail getCurrentUserDetail(Principal principal) {
        User currentUser = userService.getUserByEmail(principal.getName()).get();
        return userDetailRepository.findByUser(currentUser);
    }

}
