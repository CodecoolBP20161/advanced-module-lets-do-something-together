package com.codecool.controller;

import com.codecool.model.Profile;
import com.codecool.model.User;
import com.codecool.model.UserEmail;
import com.codecool.repository.InterestRepository;
import com.codecool.repository.ProfileRepository;
import com.codecool.repository.UserEmailRepository;
import com.codecool.security.Role;
import com.codecool.security.service.user.UserService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    ProfileRepository profileRepository;
    @Autowired
    InterestRepository interestRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserEmailRepository userEmailRepository;


    /*@RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration() {
        return "registration";
    }*/

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public
    @ResponseBody
    String registration(@RequestBody String data) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JSONObject json = new JSONObject(data);
            if (json.get("email").toString().contains("@") && json.get("password").toString().length() >= 6) {
                User user = mapper.readValue(data, User.class);
                if (userService.getUserByEmail(user.getEmail()).equals(Optional.empty())) {
                    userService.create(user, Role.USER);
                    Profile profile = new Profile(user);
                    profileRepository.save(profile);
                    UserEmail userEmail = new UserEmail(user);
                    userEmailRepository.save(userEmail);
                    return "success";
                }
            }
        } catch (IOException | JSONException e) {
            e.getMessage();
        }
        return "fail";
    }


    @RequestMapping(value = "/androidlogin", method = RequestMethod.POST)
    public
    @ResponseBody
    String androidLogin(@RequestBody String data) {
        ObjectMapper mapper = new ObjectMapper();
//        ignore password confirmation field
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            User user = mapper.readValue(data, User.class);
            if (!userService.getUserByEmail(user.getEmail()).equals(Optional.empty())) {
                if (bCryptPasswordEncoder.matches(user.getPassword(), userService.getUserByEmail(user.getEmail()).get().getPassword())) {
                    return "success";
                }
                return "wrong password";
            }
            return "register";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
