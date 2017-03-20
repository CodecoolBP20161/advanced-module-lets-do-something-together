package com.codecool.controller;

import com.codecool.repository.EventRepository;
import com.codecool.repository.UserEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequestMapping(value = "/admin")
@Controller
public class AdminController extends AbstractController {

    @Autowired
    private UserEmailRepository userEmailRepository;

    @Autowired
    private EventRepository eventRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String mainUI() {
        return "admin/admin_main";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/admin_users";
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public String listEvents(Model model) {
        model.addAttribute("events", eventRepository.findAll());
        return "admin/admin_events";
    }

    @RequestMapping(value = "/events", method = RequestMethod.POST)
    public String listEvents(HttpServletRequest request, Model model) throws IOException {
        model.addAttribute("events", eventRepository.findAll());
        return "admin/admin_events";
    }

    @RequestMapping(value = "/emails", method = RequestMethod.GET)
    public String listUserWithUnsentEmails(Model model) {
        model.addAttribute("users", userEmailRepository.findAllByEmailSent(false));
        return "admin/admin_emails";
    }
}
