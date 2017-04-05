package com.codecool.controller;

import com.codecool.repository.ProfileRepository;
import com.codecool.repository.UserEmailRepository;
import com.codecool.security.service.user.UserService;
import com.codecool.test.AbstractTest;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class UserControllerTest extends AbstractTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserEmailRepository userEmailRepository;

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() throws JSONException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
                userEmailRepository.findAllByEmailSent(false).get(0).getUser().getEmail());
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