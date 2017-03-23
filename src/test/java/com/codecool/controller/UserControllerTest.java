package com.codecool.controller;

import com.codecool.repository.ProfileRepository;
import com.codecool.repository.UserEmailRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class UserControllerTest extends AbstractTestController {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserEmailRepository userEmailRepository;

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
                .content("{\"email\":\"apple@apple.com\",\"password\":\"123456\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        assertEquals("apple@apple.com",
                userService.getUserByEmail("apple@apple.com").get().getEmail());
    }

    @Test
    public void findUnsentEmailTest() throws Exception {

        mockMvc.perform(post("/registration")
                .content("{\"email\":\"apple@apple.com\",\"password\":\"123456\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        assertEquals("apple@apple.com",
                userEmailRepository.findAllByEmailSent(false).get(0).getUser().getEmail());
    }

    @Test
    public void findRegisteredProfileTest() throws Exception {

        mockMvc.perform(post("/registration")
                .content("{\"email\":\"apple@apple.com\",\"password\":\"123456\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        assertEquals("apple@apple.com",
                profileRepository.findByUser(userService.getUserByEmail("apple@apple.com").get()).getFirstName());
    }
}