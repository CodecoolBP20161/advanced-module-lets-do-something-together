package com.codecool.controller;

import com.codecool.model.Interest;
import com.codecool.model.User;
import com.codecool.repository.InterestRepository;
import com.codecool.security.Role;
import com.codecool.security.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    private List<String> basicActivities = new ArrayList<>(
            Arrays.asList("tennis", "gokart", "running", "card games",
                    "cinema", "theater", "urban walks"));

    @Autowired
    private InterestRepository interestRepository;

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
        if(interestRepository.findAll().isEmpty()){
            basicActivities.forEach(activity -> interestRepository.save(new Interest(activity)));
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
