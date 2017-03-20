package com.codecool.controller;

import com.codecool.model.User;
import com.codecool.model.UserEmail;
import com.codecool.repository.UserEmailRepository;
import com.codecool.security.Role;
import com.codecool.security.service.user.UserService;
import com.codecool.test.AbstractTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AdminControllerTest extends AbstractTest {

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private String adminRoute;
    private String usersRoute;
    private String activitiesRoute;
    private String emailRoute;

    @Autowired
    private UserService userService;

    @Autowired
    private UserEmailRepository userEmailRepository;

    private User mockUser;

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
        usersRoute = adminRoute + "/users";
        activitiesRoute = adminRoute + "/activities";
        emailRoute = adminRoute + "/emails";

        mockUser = new User("test@test.com", "password");
    }

    @After
    public void tearDown() {
        userEmailRepository.deleteAll();
        userService.deleteAllUsers();
    }

    @Test
    public void mainUINoLoggedInUserRedirectToLogin() throws Exception {
        mockMvc.perform(get(adminRoute))
                .andExpect(unauthenticated())
                .andExpect(status().is3xxRedirection());

        String expectedRoute = "/login";
        String location = mockMvc.perform(get(adminRoute)).andReturn()
                .getResponse().getHeader("location");
        String actualRoute = location.substring(location.length() - expectedRoute.length(), location.length());
        assertEquals(true, expectedRoute.equals(actualRoute));
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
                .andExpect(content().string(containsString("ACTIVITIES")))
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
        mockMvc.perform(get(usersRoute))
                .andExpect(content().string(containsString(mockUser.getEmail())));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void listActivitiesView() throws Exception {
        mockMvc.perform(get(activitiesRoute))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get(activitiesRoute))
                .andExpect(content().string(containsString("ActiMate Admin")))
                .andExpect(content().string(containsString("List of ActiMate activities")));
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
        UserEmail userEmail = new UserEmail();
        userEmail.setUser(mockUser);
        userEmailRepository.save(userEmail);

        mockMvc.perform(get(emailRoute))
                .andExpect(content().string(containsString(mockUser.getEmail())));
    }
}