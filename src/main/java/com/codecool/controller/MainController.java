package com.codecool.controller;

import com.codecool.model.Contact;
import com.codecool.model.Interest;
import com.codecool.repository.ContactRepository;
import com.codecool.repository.InterestRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.security.Timestamp;
import java.util.Date;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private ContactRepository contactRepository;

    @RequestMapping(value = {"/", "logout"}, method = RequestMethod.GET)
    public String index() {
        return "index";
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

    @ResponseBody
    @RequestMapping(value = "/interests", method = RequestMethod.GET)
    public String interests() throws JSONException {
        return new JSONObject()
                .put("interests", interestRepository.findAll()
                        .stream().map(Interest::getActivity)
                        .collect(Collectors.toList())).toString();
    }

    @ResponseBody
    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public String contact(@RequestBody String data) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            Contact contact = mapper.readValue(data, Contact.class);
            contact.setDate(new Date());
            contactRepository.save(contact);
        } catch (IOException e) {
            e.getMessage();
        }
        return "success";
    }
}