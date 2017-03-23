package com.codecool.controller;

import com.codecool.test.AbstractTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;


public class AbstractControllerTest extends AbstractTest {

    String host = "http://localhost";

    @Resource
    FilterChainProxy springSecurityFilterChain;

    @Resource
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

}
