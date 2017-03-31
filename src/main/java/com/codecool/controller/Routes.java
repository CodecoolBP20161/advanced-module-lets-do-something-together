package com.codecool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Routes {

    @RequestMapping({
            "/",
            "/u/profile",
            "/u/dashboard",
            "/u/edit-profile",
            "/u/create_event",
            "/registration",
            "/login"
    })
    public String index() {
        return "index";
    }
}