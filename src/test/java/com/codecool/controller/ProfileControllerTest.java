package com.codecool.controller;


import com.codecool.model.Profile;
import com.codecool.model.User;
import com.codecool.repository.InterestRepository;
import com.codecool.repository.ProfileRepository;
import com.codecool.security.Role;
import com.codecool.security.service.user.UserService;
import com.codecool.test.AbstractTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
public class ProfileControllerTest extends AbstractTest {

    @Autowired
    InterestRepository interestRepository;
    @Resource
    private WebApplicationContext webApplicationContext;
    @Resource
    private FilterChainProxy springSecurityFilterChain;
    private MockMvc mockMvc;
    private String profileRoute;
    private String editProfileRoute;
    private String host;
    private User mockUser;
    private Profile profile;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileRepository profileRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .addFilters(springSecurityFilterChain)
                .build();

        mockUser = new User("apple@apple.com", "password");
        profileRoute = "/u/profile";
        editProfileRoute = "/u/edit-profile";
        host = "http://localhost";
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
                .andExpect(redirectedUrl(host + "/login"));
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

        mockMvc.perform(get(editProfileRoute))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Your Profile")))
                .andExpect(content().string(containsString("Submit")));
    }


    @Test
    @WithMockUser(value = "apple@apple.com")
    public void defaultProfileTest() throws Exception {
        userService.create(mockUser, Role.USER);
        profile = new Profile(mockUser);
        profileRepository.save(profile);
        assertNotEquals("urdu", profileRepository.findByUser(mockUser).getLanguage());
    }

    @Test
    @WithMockUser(value = "apple@apple.com")
    public void saveProfileLastNameTest() throws Exception {
        userService.create(mockUser, Role.USER);
        profile = new Profile(mockUser);
        profileRepository.save(profile);
        String profileString =
                "{\"firstName\":\"littleDog\"," +
                        "\"lastName\":\"apple\"}";
        mockMvc.perform(post(editProfileRoute)
                .content(profileString)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).
                andExpect(status().is2xxSuccessful());
        assertEquals("apple", profileRepository.findByUser(mockUser).getLastName());
    }

    @Test
    @WithMockUser(value = "apple@apple.com")
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
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        System.out.println(profile);
        System.out.println("--> " + profileRepository.findAll());
        assertEquals("urdu", profileRepository.findByUser(mockUser).getLanguage());
    }

    @Test
    @WithMockUser(value = "apple@apple.com")
    public void saveProfileGenderTest() throws Exception {
        userService.create(mockUser, Role.USER);
        profile = new Profile(mockUser);
        profileRepository.save(profile);
        String profileString =
                "{\"firstName\":\"littleDog\"," +
                        "\"gender\":\"humen\"}";
        mockMvc.perform(post(editProfileRoute)
                .content(profileString)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).
                andExpect(status().is2xxSuccessful());
        assertEquals("humen", profileRepository.findByUser(mockUser).getGender());
    }

    @Test
    @WithMockUser(value = "apple@apple.com")
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
                .accept(MediaType.APPLICATION_JSON)).
                andExpect(status().is2xxSuccessful());
        assertEquals("I'm M", profileRepository.findByUser(mockUser).getIntroduction());
    }
}