package com.codecool.controller;

import com.codecool.repository.EventRepository;
import com.codecool.security.Role;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.util.NestedServletException;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Transactional
public class EventControllerTest extends AbstractTestController {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private EventRepository eventRepository;
    private String route;

    @Before
    public void setup() {
        initMockMvc();
        route = "/u/create_event";
    }

    @After
    public void tearDown() {
        eventRepository.deleteAll();
        userService.deleteAllUsers();
    }

    @Test
    public void createEventFormUnavailableWithoutLogin() throws Exception {
        mockMvc.perform(get(route))
                .andExpect(unauthenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(loginRoute));
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
    public void createEventTest_validDetails() throws Exception {
        userService.create(mockUser, Role.USER);
        int eventsBefore = eventRepository.findAll().size();
        mockMvc.perform(post(route)
                .content("{\"name\":\"eventName\"," +
                        "\"interest\":\"other\", " +
                        "\"lng\":\"47.505013\", " +
                        "\"lat\":\"19.057821\", " +
                        "\"location\":\"Budapest, Nagymező utca 44, Hungary\", " +
                        "\"date\":\"04/12/2017 2:15 PM\", " +
                        "\"participants\":\"42\", \"description\":\"none\"}")
                .header("X-AUTH-TOKEN", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        int eventsAfter = eventRepository.findAll().size();

        assertEquals(eventsBefore + 1, eventsAfter);
        assertEquals(42, eventRepository.findAll().get(0).getParticipants());
    }

    @Test
    @WithMockUser(username = "user@user.com")
    public void createEventTest_invalidDetails_noExceptionThrown() throws Exception {
        userService.create(mockUser, Role.USER);
        mockMvc.perform(post(route)
                .content("{\"name\":\"eventName\"," +
                        "\"interest\":\"somethingNotExisting\", " +
                        "\"lng\":\"47.505013\", " +
                        "\"lat\":\"19.057821\", " +
                        "\"invalidKey\":\"2017-03-15T23:00:00.000Z\", " +
                        "\"participants\":\"42\", \"description\":\"none\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@user.com")
    public void createEventTest_missingDetails_throwsNestedJsonException() throws Exception {
        userService.create(mockUser, Role.USER);
        mockMvc.perform(post(route)
                .content("{\"name\":\"eventName\"," +
                        "\"participants\":\"42\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON));

        expectedException.expectCause(isA(JSONException.class));
        throw new NestedServletException("NestedServletException", new JSONException("JSONException"));
    }

}