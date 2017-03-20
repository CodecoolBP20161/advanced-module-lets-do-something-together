package com.codecool.controller;


import com.codecool.model.User;
import com.codecool.model.UserDetail;
import com.codecool.repository.UserDetailRepository;
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
public class ProfileControllerTest extends AbstractTest{

    @Resource
    private WebApplicationContext webApplicationContext;

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;
    private String profileRoute;
    private String editProfileRoute;
    private String host;
    private User mockUser;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailRepository userDetailRepository;

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
        userDetailRepository.deleteAll();
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
        UserDetail userDetail = new UserDetail(mockUser);
        userDetailRepository.save(userDetail);

        mockMvc.perform(get(editProfileRoute))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Your Profile")))
                .andExpect(content().string(containsString("Submit")));
    }


    @Test
    @WithMockUser(value = "apple@apple.com")
    public void defaultProfileTest() throws Exception {
        userService.create(mockUser, Role.USER);
        UserDetail userDetail = new UserDetail(mockUser);
        userDetailRepository.save(userDetail);
        assertNotEquals("urdu", userDetailRepository.findAll().get(0).getLanguage());
    }

    @Test
    @WithMockUser(value = "apple@apple.com")
    public void saveProfileLastNameTest() throws Exception {
        userService.create(mockUser, Role.USER);
        UserDetail userDetail = new UserDetail(mockUser);
        userDetailRepository.save(userDetail);
        String profile =
                "{\"firstName\":\"littleDog\"," +
                "\"lastName\":\"apple\"}";
        mockMvc.perform(post(editProfileRoute).
                content(profile)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).
                andExpect(status().is2xxSuccessful());
        assertEquals("apple", userDetailRepository.findAll().get(0).getLastName());
    }

    @Test
    @WithMockUser(value = "apple@apple.com")
    public void saveProfileLanguageTest() throws Exception {
        userService.create(mockUser, Role.USER);
        UserDetail userDetail = new UserDetail(mockUser);
        userDetailRepository.save(userDetail);
        String profile =
                "{\"firstName\":\"littleDog\"," +
                "\"language\":\"urdu\"}";
        mockMvc.perform(post(editProfileRoute).
                content(profile)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).
                andExpect(status().is2xxSuccessful());
        assertEquals("urdu", userDetailRepository.findAll().get(0).getLanguage());
    }

    @Test
    @WithMockUser(value = "apple@apple.com")
    public void saveProfileGenderTest() throws Exception {
        userService.create(mockUser, Role.USER);
        UserDetail userDetail = new UserDetail(mockUser);
        userDetailRepository.save(userDetail);
        String profile =
                "{\"firstName\":\"littleDog\"," +
                 "\"gender\":\"humen\"}";
        mockMvc.perform(post(editProfileRoute).
                content(profile)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).
                andExpect(status().is2xxSuccessful());
        assertEquals("humen", userDetailRepository.findAll().get(0).getGender());
    }

    @Test
    @WithMockUser(value = "apple@apple.com")
    public void saveProfileIntroductionTest() throws Exception {
        userService.create(mockUser, Role.USER);
        UserDetail userDetail = new UserDetail(mockUser);
        userDetailRepository.save(userDetail);
        String profile =
                "{\"firstName\":\"littleDog\"," +
                "\"introduction\":\"I'm M\"}";
        mockMvc.perform(post(editProfileRoute).
                content(profile)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).
                andExpect(status().is2xxSuccessful());
        assertEquals("I'm M", userDetailRepository.findAll().get(0).getIntroduction());
    }

    @Test
    @WithMockUser(value = "apple@apple.com")
    public void saveProfileInterestTest() throws Exception {
        userService.create(mockUser, Role.USER);
        UserDetail userDetail = new UserDetail(mockUser);
        userDetailRepository.save(userDetail);
        String profile =
                "{\"firstName\":\"littleDog\"," +
                "\"interest\":{\"gokart\":\"true\"}}";
        mockMvc.perform(post(editProfileRoute).
                content(profile)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).
                andExpect(status().is2xxSuccessful());
        assertEquals("gokart", userDetailRepository.findAll().get(0).getInterestList().get(0).getActivity());
    }
}