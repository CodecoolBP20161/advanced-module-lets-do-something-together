package com.codecool.controller;

import com.codecool.security.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/", "logout"}, method = RequestMethod.GET)
    public String index(String logout) {
        return "index";
    }


    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(Model model, String error) {
        if (error != null) {
            model.addAttribute("error", "Failed to log in. Email or password is invalid!");
        }
        return "login_form";
    }

    @RequestMapping(value = "/mission", method = RequestMethod.GET)
    public String mission() {
        return "mission";
    }
}