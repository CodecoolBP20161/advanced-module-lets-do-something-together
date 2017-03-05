package com.codecool.controller;

import com.codecool.model.Interest;
import com.codecool.model.User;
import com.codecool.model.UserDetail;
import com.codecool.repository.InterestRepository;
import com.codecool.repository.UserDetailRepository;
import com.codecool.security.Role;
import com.codecool.security.service.user.UserService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
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
import java.lang.reflect.Field;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserDetailRepository userDetailRepository;
    @Autowired
    InterestRepository interestRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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

    @RequestMapping(value = "/edit-profile", method = RequestMethod.GET)
    public String profile() {
        return "profile_form";
    }

    @RequestMapping(value = "/edit-profile", method = RequestMethod.POST)
    public String profile(@RequestBody String data, Principal principal) throws JSONException, IllegalAccessException {
        UserDetail currentUserDetail = getCurrentUserDetail(principal);
        List<Field> fields = getEditableFieldsOfCurrentUserDetail(currentUserDetail);

//            profile related JSONExceptions swallowed on purpose: not mandatory profile details
        JSONObject jsonData = new JSONObject(data);

        for (Field field : fields.subList(0, fields.size() - 1)) {
            field.setAccessible(true);
            String fieldValue = null;
            try {
                fieldValue = jsonData.get(field.getName()).toString();
            } catch (JSONException ignored) {
            }
            field.set(currentUserDetail, fieldValue);
        }
        currentUserDetail.setInterestList(getInterestsFromJson(jsonData));
        userDetailRepository.save(currentUserDetail);
        return "profile_form";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String dashboard() {
        return "profile";
    }

    private List<Interest> getInterestsFromJson(JSONObject jsonObject) {
        List<Interest> interestList = new ArrayList<>();
        try {
            JSONArray jsonArray = ((JSONObject) jsonObject.get("interest")).names();
            for (int i = 0; i < jsonArray.length(); i++) {
                interestList.add(interestRepository.findByActivity(jsonArray.getString(i)));
            }
        } catch (JSONException ignored) {
        }
        return interestList;
    }

    private UserDetail getCurrentUserDetail(Principal principal) {
        User currentUser = userService.getUserByEmail(principal.getName()).get();
        return userDetailRepository.findByUser(currentUser);
    }

    private List<Field> getEditableFieldsOfCurrentUserDetail(UserDetail userDetail) {
        Field[] fieldsArray = userDetail.getClass().getDeclaredFields();
        return Arrays.asList(fieldsArray).subList(2, fieldsArray.length);
    }
}