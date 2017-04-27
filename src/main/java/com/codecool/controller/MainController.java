package com.codecool.controller;

import com.codecool.model.Contact;
import com.codecool.model.Interest;
import com.codecool.repository.ContactRepository;
import com.codecool.repository.InterestRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private ContactRepository contactRepository;

//    @RequestMapping(value = "/mission", method = RequestMethod.GET)
//    public String mission() {
//        logger.info("/mission route called - method: {}.", RequestMethod.GET);
//        return "mission";
//    }

    @ResponseBody
    @RequestMapping(value = "/interests", method = RequestMethod.GET)
    public String interests() throws JSONException {
        logger.info("/interests route called - method: {}.", RequestMethod.GET);
        return new JSONObject()
                .put("interests", interestRepository.findAll()
                        .stream().map(Interest::getActivity)
                        .collect(Collectors.toList())).toString();
    }

    @ResponseBody
    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public String contact(@RequestBody String data) throws JSONException {
        logger.info("/contact route called - method: {}.", RequestMethod.GET);
        JSONObject result = new JSONObject().put("status", "fail");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            Contact contact = mapper.readValue(data, Contact.class);
            contact.setDate(new Date());
            contactRepository.save(contact);
            logger.info("New contact from '{}' at {} saved.", contact.getName(), contact.getDate());
            result.put("status", "success");
        } catch (IOException e) {
            e.getMessage();
        }
        logger.info("New contact status is: '{}'.", result.get("status"));
        return result.toString();
    }
}