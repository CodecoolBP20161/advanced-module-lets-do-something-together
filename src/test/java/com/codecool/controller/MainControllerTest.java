package com.codecool.controller;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MainControllerTest extends AbstractTestController {

    private String route;

    @Before
    public void setup() {
        initMockMvc();
        route = "/";
    }

    @Test
    public void mainPageTest() throws Exception {

        mockMvc.perform(get(route))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get(route))
                .andExpect(content().string(containsString("Actimate")));

        mockMvc.perform(get("/notvalidroute"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void loginPageTest() throws Exception {
        String login = "/login";
        mockMvc.perform(get(login))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get(login))
                .andExpect(content().string(containsString("LOGIN")))
                .andExpect(content().string(containsString("PASSWORD")))
                .andExpect(content().string(containsString("EMAIL")));
    }
}