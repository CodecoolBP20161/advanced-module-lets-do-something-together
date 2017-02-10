package com.codecool.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {


    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index() {
        return "index";
    }


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration() {
        return "csáá";
    }
}
