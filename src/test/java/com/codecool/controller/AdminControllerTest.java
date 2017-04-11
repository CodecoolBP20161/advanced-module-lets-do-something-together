package com.codecool.controller;

import com.codecool.email.model.WelcomeEmail;
import com.codecool.email.repository.WelcomeEmailRepository;
import com.codecool.security.Role;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
public class AdminControllerTest extends AbstractTestController {

    private String adminRoute;
    private String usersRoute;
    private String eventsRoute;
    private String emailRoute;

    @Autowired
    private WelcomeEmailRepository welcomeEmailRepository;

    @Before
    public void setup() {
        initMockMvc();
        adminRoute = "/admin";
        usersRoute = adminRoute + "/users";
        eventsRoute = adminRoute + "/events";
        emailRoute = adminRoute + "/emails";
    }

    @After
    public void tearDown() {
        welcomeEmailRepository.deleteAll();
        userService.deleteAllUsers();
    }

    @Test
    public void mainUINoLoggedInUserRedirectToLogin() throws Exception {
        mockMvc.perform(get(adminRoute))
                .andExpect(unauthenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(loginRoute));
    }

    @Test
    @WithMockUser
    public void mainUIWithUserRoleForbidden() throws Exception {
        mockMvc.perform(get(adminRoute))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void mainUIWithAdminRoleAccessGranted() throws Exception {
        mockMvc.perform(get(adminRoute))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get(adminRoute))
                .andExpect(content().string(containsString("USERS")))
                .andExpect(content().string(containsString("EVENTS")))
                .andExpect(content().string(containsString("EMAILS")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void listUsersView() throws Exception {

        mockMvc.perform(get(usersRoute))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get(usersRoute))
                .andExpect(content().string(containsString("ActiMate Admin")))
                .andExpect(content().string(containsString("List of ActiMate users")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void listUsersData() throws Exception {
        userService.create(mockUser, Role.USER);
        mockMvc.perform(get(usersRoute)
                .header("X-AUTH-TOKEN", UUID.randomUUID().toString()))
                .andExpect(content().string(containsString(mockUser.getEmail())));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void listActivitiesView() throws Exception {
        mockMvc.perform(get(eventsRoute))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get(eventsRoute)
                .header("X-AUTH-TOKEN", UUID.randomUUID().toString()))
                .andExpect(content().string(containsString("ActiMate Admin")))
                .andExpect(content().string(containsString("List of ActiMate events")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void listUsersWithUnsentEmailView() throws Exception {
        mockMvc.perform(get(emailRoute))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get(emailRoute))
                .andExpect(content().string(containsString("ActiMate Admin")))
                .andExpect(content().string(containsString("List of unsent emails")))
                .andExpect(content().string(containsString("REGISTRATION DATE")));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void listUsersWithUnsentEmailData() throws Exception {
        userService.create(mockUser, Role.USER);
        WelcomeEmail welcomeEmail = new WelcomeEmail(mockUser);
        welcomeEmailRepository.save(welcomeEmail);

        mockMvc.perform(get(emailRoute)
                .header("X-AUTH-TOKEN", UUID.randomUUID().toString()))
                .andExpect(content().string(containsString(mockUser.getEmail())));
    }
}