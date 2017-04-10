package com.codecool.controller;


import com.codecool.model.Profile;
import com.codecool.repository.InterestRepository;
import com.codecool.repository.ProfileRepository;
import com.codecool.security.Role;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Repeat;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
public class ProfileControllerTest extends AbstractTestController {

    @Autowired
    InterestRepository interestRepository;

    private String profileRoute;
    private String editProfileRoute;
    private Profile profile;

    @Autowired
    private ProfileRepository profileRepository;

    @Before
    public void setup() {
        initMockMvc();
        profileRoute = "/u/profile";
        editProfileRoute = "/u/edit-profile";
    }

    @After
    public void tearDown() {
        profileRepository.deleteAll();
        userService.deleteAllUsers();
    }

    @Test
    public void notLoggedInRedirectToLoginPageTest() throws Exception {
        mockMvc.perform(get(profileRoute))
                .andExpect(unauthenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(loginRoute));
    }

    @Test
    @WithMockUser
    public void loggedInUserCanSeeProfileTest() throws Exception {
        mockMvc.perform(get(profileRoute))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Your Profile")));
    }

    @Test
    @WithMockUser
    public void returnProfileTest() throws Exception {
        userService.create(mockUser, Role.USER);
        profile = new Profile(mockUser);
        profileRepository.save(profile);

        mockMvc.perform(get(editProfileRoute)
                .header("X-AUTH-TOKEN", UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Your Profile")))
                .andExpect(content().string(containsString("Submit")));
    }


    @Test
    @WithMockUser(value = "user@user.com")
    public void defaultProfileTest() throws Exception {
        userService.create(mockUser, Role.USER);
        profile = new Profile(mockUser);
        profileRepository.save(profile);
        assertNotEquals("urdu", profileRepository.findByUser(mockUser).getLanguage());
    }

    @Test
    @WithMockUser(value = "user@user.com")
    public void saveProfileLastNameTest() throws Exception {
        userService.create(mockUser, Role.USER);
        profile = new Profile(mockUser);
        profileRepository.save(profile);

        String profileString =
                "{\"firstName\":\"littleDog\"," +
                        "\"lastName\":\"user\"}";

        mockMvc.perform(post(editProfileRoute)
                .content(profileString)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)).
                andExpect(status().is2xxSuccessful());

        assertEquals("user", profileRepository.findByUser(mockUser).getLastName());
    }

    @Test
    @WithMockUser(value = "user@user.com")
    public void saveProfileLanguageTest() throws Exception {
        userService.create(mockUser, Role.USER);
        profile = new Profile(mockUser);
        profileRepository.save(profile);

        String profileString =
                "{\"firstName\":\"littleDog\"," +
                        "\"language\":\"urdu\"}";

        mockMvc.perform(post(editProfileRoute)
                .content(profileString)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        assertEquals("urdu", profileRepository.findByUser(mockUser).getLanguage());
    }

    @Test
    @WithMockUser(value = "user@user.com")
    public void saveProfileGenderTest() throws Exception {
        userService.create(mockUser, Role.USER);
        profile = new Profile(mockUser);
        profileRepository.save(profile);

        String profileString =
                "{\"firstName\":\"littleDog\"," +
                        "\"gender\":\"human\"}";

        mockMvc.perform(post(editProfileRoute)
                .content(profileString)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        assertEquals("human", profileRepository.findByUser(mockUser).getGender());
    }

    @Test
    @WithMockUser(value = "user@user.com")
    public void saveProfileIntroductionTest() throws Exception {
        userService.create(mockUser, Role.USER);
        profile = new Profile(mockUser);
        profileRepository.save(profile);

        String profileString =
                "{\"firstName\":\"littleDog\"," +
                        "\"introduction\":\"I'm M\"}";

        mockMvc.perform(post(editProfileRoute)
                .content(profileString)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        assertEquals("I'm M", profileRepository.findByUser(mockUser).getIntroduction());
    }

    @Test
    @WithMockUser(value = "user@user.com")
    public void saveProfileInterestsTest() throws Exception {
        userService.create(mockUser, Role.USER);
        profile = new Profile(mockUser);
        profileRepository.save(profile);

        String profileString =
                "{\"firstName\":\"littleDog\"," +
                        "\"interest\":[\"running\",\"gokart\"]}";

        mockMvc.perform(post(editProfileRoute)
                .content(profileString)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        assertTrue(profileRepository.findByUser(mockUser).getInterestList().contains(interestRepository.findByActivity("gokart")));
        assertTrue(profileRepository.findByUser(mockUser).getInterestList().contains(interestRepository.findByActivity("running")));
        assertFalse(profileRepository.findByUser(mockUser).getInterestList().contains(interestRepository.findByActivity("someInterest")));
    }

}