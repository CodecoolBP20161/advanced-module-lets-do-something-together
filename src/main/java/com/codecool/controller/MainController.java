package com.codecool.controller;

import com.codecool.model.Interest;
import com.codecool.repository.InterestRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    private InterestRepository interestRepository;

    @ResponseBody
    @RequestMapping(value = "/interests", method = RequestMethod.GET)
    public String interests() throws JSONException {
        return new JSONObject()
                .put("interests", interestRepository.findAll()
                        .stream().map(Interest::getActivity)
                        .collect(Collectors.toList())).toString();
    }
}