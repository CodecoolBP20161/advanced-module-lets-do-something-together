package com.codecool.controller;

import com.codecool.model.User;
import com.codecool.security.Role;
import com.codecool.security.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/", "logout"}, method = RequestMethod.GET)
    public String index(Model model, String logout) {
//        TODO delete this
//        default admin user to test login/access/etc
        if (userService.getUserByEmail("admin@admin.com").equals(Optional.empty()) ||
                userService.getUserByEmail("admin@admin.com") == null) {
            User admin = new User("admin@admin.com", "1234");
            userService.create(admin, Role.ADMIN);
        }
        return "main";
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
