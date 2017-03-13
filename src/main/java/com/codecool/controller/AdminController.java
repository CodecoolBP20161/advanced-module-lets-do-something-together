package com.codecool.controller;

import com.codecool.model.Interest;
import com.codecool.model.event.Event;
import com.codecool.model.event.Status;
import com.codecool.repository.EventRepository;
import com.codecool.repository.InterestRepository;
import com.codecool.repository.UserEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping(value = "/admin")
@Controller
public class AdminController extends AbstractController {

    @Autowired
    private UserEmailRepository userEmailRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private InterestRepository interestRepository;

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
     public String listEvents(HttpServletRequest request, Model model) {
         String key = request.getParameter("key").toLowerCase();
         String filter = request.getParameter("filter");

         switch (filter) {
             case "status":
                 if (key.equals("active")) {
                     Status status = Status.ACTIVE;
                     List<Event> events = eventRepository.findByStatus(status);
                     model.addAttribute("events", events);
                 } else {
                     Status status = Status.PAST;
                     List<Event> events = eventRepository.findByStatus(status);
                     model.addAttribute("events", events);
                 }
             case "date":
                 List<Event> events = eventRepository.findByDate(key);
                 model.addAttribute("events", events);
             case "activities":
                 Interest interest = interestRepository.findByActivity(key);
                 events = eventRepository.findByInterest(interest);
                 model.addAttribute("events", events);
         }
         return "admin/admin_events";
     }

    @RequestMapping(value = "/email", method = RequestMethod.GET)
    public String listUserWithUnsentEmails(Model model) {
        model.addAttribute("users", userEmailRepository.findAllByEmailSent(false));
        return "admin/email";
    }
}
