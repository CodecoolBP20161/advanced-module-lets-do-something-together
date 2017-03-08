package com.codecool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @RequestMapping(value = {"/", "logout"}, method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/usermain", method = RequestMethod.GET)
    public String userMain() {
        return "user_main";
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