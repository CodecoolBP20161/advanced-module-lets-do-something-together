package com.codecool.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    @Test
    @WithMockUser
    public void dashboardRenderedWhenLoggedin() throws Exception {
        //        TODO test for content when there's actually something relevant on the dashboard page
        mockMvc.perform(get(route))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("profile")))
                .andExpect(content().string(containsString("logout")));
    }

}