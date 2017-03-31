package com.codecool.security.service;


import com.codecool.model.ApiAuthentication;
import com.codecool.model.User;
import com.codecool.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;


public class ApiAuthenticationService {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
    private static final Logger logger = LoggerFactory.getLogger(ApiAuthenticationService.class);


    @Autowired
    UserRepository userRepository;


    public Authentication getAuth(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(AUTH_HEADER_NAME);
        System.out.println("!!!!!!" +  token);
        User user = userRepository.findByToken(token);
        System.out.println(token);
        if (user == null) {
            logger.info("No user found");
            throw new AuthenticationCredentialsNotFoundException("No user found");
        }
        System.out.println("!!!!!!" +  token);
        if (user.getToken() == null) {
            logger.info("No token found");
            throw new AuthenticationCredentialsNotFoundException("No token found");
        }
        logger.info("Token found");
        return new ApiAuthentication(user);
    }
}
