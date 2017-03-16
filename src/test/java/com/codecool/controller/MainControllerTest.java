package com.codecool.controller;

import com.codecool.test.AbstractTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.annotation.Resource;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



public class MainControllerTest extends AbstractTest {

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void mainPageTest() throws Exception {

        mockMvc.perform(get("/"))
            .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/"))
            .andExpect(content().string(containsString("HOME")));

        mockMvc.perform(get("/notvalidroute"))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void loginPageTest() throws Exception {

        mockMvc.perform(get("/login"))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/login"))
                .andExpect(content().string(containsString("LOGIN")))
                .andExpect(content().string(containsString("PASSWORD")))
                .andExpect(content().string(containsString("EMAIL")));
    }
}