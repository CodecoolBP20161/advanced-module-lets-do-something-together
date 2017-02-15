package com.codecool.controller;

import com.codecool.model.User;
import com.codecool.security.Role;
import com.codecool.security.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody String registration(@RequestBody String data) {
//        JSON from String to Object
        ObjectMapper mapper = new ObjectMapper();
        try {
            User user = mapper.readValue(data, User.class);
            if (userService.getUserByEmail(user.getEmail()).equals(Optional.empty())) {
                userService.create(user, Role.USER);
            } else {
                return "registration";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
}
