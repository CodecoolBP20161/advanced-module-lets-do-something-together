package com.codecool.controller;

import com.codecool.repository.UserEmailRepository;
import com.codecool.security.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/admin")
@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserEmailRepository userEmailRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String mainUI() {
        return "admin/admin_main";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/admin_users";
    }

    @RequestMapping(value = "/activities", method = RequestMethod.GET)
    public String listActivities(Model model) {
        model.addAttribute("activities", "NotImplementedError or whatevs");
        return "admin/admin_activities";
    }

    @RequestMapping(value = "/email", method = RequestMethod.GET)
    public String listUserWithUnsentEmails(Model model) {
        model.addAttribute("users", userEmailRepository.findAllByEmailSent(false));
        return "admin/email";
    }
}
