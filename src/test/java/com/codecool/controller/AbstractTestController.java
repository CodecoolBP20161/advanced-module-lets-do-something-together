package com.codecool.controller;

import com.codecool.model.User;
import com.codecool.security.service.user.UserService;
import com.codecool.test.AbstractTest;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


abstract class AbstractTestController extends AbstractTest {

    String host = "http://localhost";
    String loginRoute = host + "/login";

    @Resource
    FilterChainProxy springSecurityFilterChain;

    @Resource
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    User mockUser = new User("user@user.com", "password");

    @Autowired
    UserService userService;

    @Before
    public void initMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .addFilters(springSecurityFilterChain)
                .build();
    }
}
