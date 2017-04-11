package com.codecool.controller;

import com.codecool.email.repository.WelcomeEmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/admin")
@Controller
public class AdminController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private WelcomeEmailRepository welcomeEmailRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String mainUI() {
        logger.info("'/admin' route called - method: {}.", RequestMethod.GET);
        return "admin/admin_main";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String listUsers(Model model) {
        logger.info("'/admin/users' route called - method: {}.", RequestMethod.GET);
        model.addAttribute("users", userService.getAllUsers());
        return "admin/admin_users";
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public String listEvents(Model model) {
        logger.info("'/admin/events' route called - method: {}.", RequestMethod.GET);
        model.addAttribute("events", eventRepository.findAll());
        return "admin/admin_events";
    }

    @RequestMapping(value = "/emails", method = RequestMethod.GET)
    public String listUsersWithUnsentEmail(Model model) {
        logger.info("'/admin/emails' route called - method: {}.", RequestMethod.GET);
        model.addAttribute("users", welcomeEmailRepository.findAllByEmailSent(false));
        return "admin/admin_emails";
    }
}
