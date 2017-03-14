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

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AdminControllerTest extends AbstractTest {

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private String adminRoute;

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .addFilters(springSecurityFilterChain)
                .build();
        adminRoute = "/admin";
    }

    @Test
    public void mainUINoLoggedInUserRedirectToLogin() throws Exception {
        mockMvc.perform(get(adminRoute))
                .andExpect(unauthenticated())
                .andExpect(status().is3xxRedirection());

        String expectedRoute = "/login";
        String location = mockMvc.perform(get(adminRoute))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse().getHeader("location");
        String actualRoute = location.substring(location.length() - expectedRoute.length(), location.length());
        assertEquals(true, expectedRoute.equals(actualRoute));
    }

    @Test
    @WithMockUser(username = "user@user.com")
    public void mainUIWithUserRoleForbidden() throws Exception {
        mockMvc.perform(get(adminRoute))
                .andExpect(status().is4xxClientError());
    }
}