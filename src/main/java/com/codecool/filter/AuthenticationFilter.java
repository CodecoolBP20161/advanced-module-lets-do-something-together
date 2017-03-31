package com.codecool.filter;


import com.codecool.security.service.ApiAuthenticationService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFilter extends GenericFilterBean{


    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    ApiAuthenticationService service;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Authentication authentication;

        try {
            authentication = service.getAuth(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("REST authentication successful with user: {}",
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (InsufficientAuthenticationException invalidToken) {
            logger.debug("REST auth failed: {}", invalidToken.getMessage());
            response.sendError(401, invalidToken.getMessage());
        } catch (AuthenticationCredentialsNotFoundException noToken) {
            logger.debug("{}, moving on to next authentication link", noToken.getMessage());
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
