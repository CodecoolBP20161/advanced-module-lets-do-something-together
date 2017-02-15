package com.codecool.controller;

import com.codecool.model.User;
import com.codecool.repository.RoleRepository;
import com.codecool.repository.UserRepository;
import com.codecool.security.Role;
import com.codecool.security.service.user.UserService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository) {
    }

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index() {
//        create possible roles *before* creating users -> so they can be referenced by the new user
//        TODO come up with a proper way to do so
        Role admin = Role.ADMIN;
        admin.setRole(admin.toString());
        Role user = Role.USER;
        user.setRole(user.toString());
        roleRepository.save(admin);
        roleRepository.save(user);
        return "index";
    }

    //    TODO remove this when done: it's a test route, example for how it should work
//    + the templates as well
    @PreAuthorize("@CurrentUserServiceImpl.canAccessUser(principal, #id)")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "test";
    }

    //    TODO remove this when done: it's a test route, example for how it should work 2
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "test2";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration() {
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody String registration(@RequestBody String data) {
//        JSON from String to Object
        ObjectMapper mapper = new ObjectMapper();
        try {
            User user = mapper.readValue(data, User.class);
            if (userRepository.findByEmail(user.getEmail()) == null) {
//                default user is USER, an admin can later create new ADMIN users as well
                userService.create(user, Role.USER);
            } else {
                return "index";
            }
        } catch (JsonGenerationException | JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
}
