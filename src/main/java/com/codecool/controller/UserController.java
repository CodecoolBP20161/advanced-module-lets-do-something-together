package com.codecool.controller;

import com.codecool.model.Profile;
import com.codecool.model.User;
import com.codecool.model.UserEmail;
import com.codecool.repository.InterestRepository;
import com.codecool.repository.ProfileRepository;
import com.codecool.repository.UserEmailRepository;
import com.codecool.repository.UserRepository;
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
import java.util.UUID;

@Controller
public class UserController {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    InterestRepository interestRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserEmailRepository userEmailRepository;


    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration() {
        return "registration";
    }

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
                    user.setToken(UUID.randomUUID());
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
    String androidLogin(@RequestBody String data ) throws JSONException {
        ObjectMapper mapper = new ObjectMapper();
        JSONObject loginResponseJson = new JSONObject();
//        ignore password confirmation field
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            User user = mapper.readValue(data, User.class);
            if (!userService.getUserByEmail(user.getEmail()).equals(Optional.empty())) {
                if (bCryptPasswordEncoder.matches(user.getPassword(), userService.getUserByEmail(user.getEmail()).get().getPassword())) {
                    loginResponseJson.put("status", "success");
                    loginResponseJson.put("token", userService.getUserByEmail(user.getEmail()).get().getToken());
                    System.out.println(loginResponseJson.toString());
                    return loginResponseJson.toString();
                }
                loginResponseJson.put("status", "wrong password");
                return loginResponseJson.toString();
            }
            System.out.println(loginResponseJson.put("status", "wrong email"));
            System.out.println(loginResponseJson.toString());
            return loginResponseJson.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
