package com.codecool.controller;

import com.codecool.test.AbstractTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class EventControllerTest extends AbstractTest {

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .addFilters(springSecurityFilterChain)
                .build();
    }

    @Test
    public void createEventFormUnavailableWithoutLogin() throws Exception {
        mockMvc.perform(get("/u/create_event")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void renderCreateEventFormTest() throws Exception {
        mockMvc.perform(get("/u/create_event")).andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/u/create_event"))
                .andExpect(content().string(containsString(" Create Event")))
                .andExpect(content().string(containsString("input type=\"text\"")))
                .andExpect(content().string(containsString("Submit")));
    }
}