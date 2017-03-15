package com.codecool.controller;

import com.codecool.model.User;
import com.codecool.repository.EventRepository;
import com.codecool.security.Role;
import com.codecool.security.service.user.UserService;
import com.codecool.test.AbstractTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class EventControllerTest extends AbstractTest {

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserService userService;

    private String route;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .addFilters(springSecurityFilterChain)
                .build();

        route = "/u/create_event";
        userService.create(new User("user@user.com", "password"), Role.USER);
    }

    @Test
    public void createEventFormUnavailableWithoutLogin() throws Exception {
        mockMvc.perform(get(route)).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void renderCreateEventFormTest() throws Exception {
        mockMvc.perform(get(route)).andExpect(status().is2xxSuccessful());

        mockMvc.perform(get(route))
                .andExpect(content().string(containsString(" Create Event")))
                .andExpect(content().string(containsString("input type=\"text\"")))
                .andExpect(content().string(containsString("Submit")));
    }

    @Test
    @WithMockUser(username = "user@user.com")
    public void createEventTest() throws Exception {
        int eventsBefore = eventRepository.findAll().size();

        mockMvc.perform(post(route)
                .content("{\"name\":\"eventName\"," +
                        "\"interest\":\"gokart\", " +
                        "\"lng\":\"47.505013\", " +
                        "\"lat\":\"19.057821\", " +
                        "\"date\":\"2017-03-15T23:00:00.000Z\", " +
                        "\"participants\":\"42\", \"description\":\"none\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).
                andExpect(status().is2xxSuccessful());

        int eventsAfter = eventRepository.findAll().size();

        assertEquals(eventsBefore + 1, eventsAfter);
        assertEquals(42, eventRepository.findAll().get(0).getParticipants());
    }

}