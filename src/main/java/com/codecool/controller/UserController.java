package com.codecool.controller;

import com.codecool.model.User;
import com.codecool.model.UserDetail;
import com.codecool.repository.InterestRepository;
import com.codecool.repository.UserDetailRepository;
import com.codecool.security.Role;
import com.codecool.security.service.user.UserService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private UserService userService;

    @Autowired
    UserDetailRepository userDetailRepository;

    @Autowired
    InterestRepository interestRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration() {
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody String registration(@RequestBody String data) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            User user = mapper.readValue(data, User.class);
            if (userService.getUserByEmail(user.getEmail()).equals(Optional.empty())) {
                userService.create(user, Role.USER);
                UserDetail userDetail = new UserDetail(user);
//                List<Interest> interest = interestRepository.findAll();
////                List<Interest> interests = new ArrayList<>(Arrays.asList(interest));
//                System.out.println("aaaaaaaaaaaaaaaaa" + interest);
//                userDetail.setInterestList(interest);
//
//                System.out.println(interest);
                userDetailRepository.save(userDetail);

            } else {
                return "fail";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }


    @RequestMapping(value = "/androidlogin", method = RequestMethod.POST)
    public @ResponseBody
    String androidLogin(@RequestBody String data) {
        ObjectMapper mapper = new ObjectMapper();
//        ignore password confirmation field
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            User user = mapper.readValue(data, User.class);
            if (!userService.getUserByEmail(user.getEmail()).equals(Optional.empty())) {
                if (bCryptPasswordEncoder.matches( user.getPassword(), userService.getUserByEmail(user.getEmail()).get().getPassword())) {
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

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile() {
        return "profile";
    }
}
