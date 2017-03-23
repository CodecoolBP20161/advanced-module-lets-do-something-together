package com.codecool.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class DashBoardControllerTest extends AbstractControllerTest {

    private String route;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .addFilters(springSecurityFilterChain)
                .build();

        route = "/u/dashboard";
    }

    @Test
    public void dashboardRedirectsWithoutLogin() throws Exception {
        mockMvc.perform(get(route))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(host + "/login"));
    }

}