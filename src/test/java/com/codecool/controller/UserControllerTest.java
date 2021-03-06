package com.codecool.controller;

import com.codecool.email.repository.WelcomeEmailRepository;
import com.codecool.repository.ProfileRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class UserControllerTest extends AbstractTestController {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private WelcomeEmailRepository welcomeEmailRepository;

    @Before
    public void setup() {
        initMockMvc();
    }

    @Test
    public void registrationPageTest() throws Exception {

        mockMvc.perform(get("/registration"))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/registration"))
                .andExpect(content().string(containsString("EMAIL")))
                .andExpect(content().string(containsString("PASSWORD")))
                .andExpect(content().string(containsString("CONFIRM PASSWORD")));
    }

    @Test
    public void findRegisteredUserTest() throws Exception {

        mockMvc.perform(post("/registration")
                .content("{\"email\":\"user@user.com\",\"password\":\"123456\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        assertEquals("user@user.com",
                userService.getUserByEmail("user@user.com").get().getEmail());
    }

    @Test
    public void findUnsentEmailTest() throws Exception {

        mockMvc.perform(post("/registration")
                .content("{\"email\":\"user@user.com\",\"password\":\"123456\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        assertEquals("user@user.com",
                welcomeEmailRepository.findAllByEmailSent(false).get(0).getUser().getEmail());
    }

    @Test
    public void findRegisteredProfileTest() throws Exception {

        mockMvc.perform(post("/registration")
                .content("{\"email\":\"user@user.com\",\"password\":\"123456\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        assertEquals("user@user.com",
                profileRepository.findByUser(userService.getUserByEmail("user@user.com").get()).getFirstName());
    }

    @Test
    public void findTokenRegisteredUserTest() throws Exception {
        mockMvc.perform(post("/registration")
                .content("{\"email\":\"user@user.com\",\"password\":\"123456\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        assertNotEquals(null, userService.getUserByEmail("user@user.com").get().getToken());
    }
}