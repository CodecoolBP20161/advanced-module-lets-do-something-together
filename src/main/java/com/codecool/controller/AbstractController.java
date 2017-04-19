package com.codecool.controller;


import com.codecool.model.Profile;
import com.codecool.model.User;
import com.codecool.repository.EventRepository;
import com.codecool.repository.InterestRepository;
import com.codecool.repository.ProfileRepository;
import com.codecool.security.service.user.UserService;
import com.codecool.util.EventUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;

abstract class AbstractController {

    @Autowired
    EventUtil eventUtil;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    InterestRepository interestRepository;

    @Autowired
    UserService userService;

    Profile getCurrentProfile(Principal principal) {
        return profileRepository.findByUser(getCurrentUser(principal));
    }

    User getCurrentUser(Principal principal) {
        return userService.getUserByEmail(principal.getName()).get();
    }

}
