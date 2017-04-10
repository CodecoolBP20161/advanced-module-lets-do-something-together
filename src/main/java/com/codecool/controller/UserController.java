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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class UserController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
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
    String registration(@RequestBody String data) throws JSONException {
        logger.info("/registration route called");
        ObjectMapper mapper = new ObjectMapper();
        JSONObject result = new JSONObject();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JSONObject json = new JSONObject(data);
            if (json.get("email").toString().contains("@") && json.get("password").toString().length() >= 6) {
                User user = mapper.readValue(data, User.class);
                if (userService.getUserByEmail(user.getEmail()).equals(Optional.empty())) {
                    user.setToken(UUID.randomUUID().toString());
                    userService.create(user, Role.USER);
                    Profile profile = new Profile(user);
                    profileRepository.save(profile);
                    logger.info("Save profile into the {} table", profile.getClass().getSimpleName());
                    UserEmail userEmail = new UserEmail(user);
                    userEmailRepository.save(userEmail);
                    logger.info("Unsent email save into the {} table", userEmail.getClass().getSimpleName());
                    return result.put("status","success").toString();
                }
            }
        } catch (IOException | JSONException e) {
            logger.error("{} occurred while creating the user: {}", e.getCause(), e.getMessage());
            e.getMessage();
        }
        return result.put("status","fail").toString();
    }

    @RequestMapping(value = "/api-login", method = RequestMethod.POST)
    public
    @ResponseBody
    String androidLogin(@RequestBody String data ) throws JSONException {
        logger.info("api-login route called");
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
                    logger.info("Successful login by {}", userService.getUserByEmail(user.getEmail()));
                    return loginResponseJson.toString();
                }
                loginResponseJson.put("status", "wrong password");
                logger.info("Wrong email during login");
                return loginResponseJson.toString();
            }
            return loginResponseJson.toString();
        } catch (IOException e) {
            logger.error("{} error occurred while login {}", e.getCause(), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
