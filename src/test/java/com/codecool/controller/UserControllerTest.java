package com.codecool.controller;

import com.codecool.model.User;
import com.codecool.repository.UserDetailRepository;
import com.codecool.repository.UserEmailRepository;
import com.codecool.repository.UserRepository;
import com.codecool.test.AbstractTest;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class UserControllerTest extends AbstractTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private UserEmailRepository userEmailRepository;

    private JdbcTemplate jdbcTemplate;

    private User user1;
    JSONObject jsonObject = new JSONObject();


    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

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
    public void FindRegisteredUserTest() throws Exception {

        mockMvc.perform(post("/registration")
                .content("{\"email\":\"apple@apple.com\",\"password\":\"123456\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        assertEquals("apple@apple.com",userRepository.findByEmail("apple@apple.com").getEmail());
    }

    @Test
    public void FindUnsentEmailTest() throws Exception {

        mockMvc.perform(post("/registration")
                .content("{\"email\":\"apple@apple.com\",\"password\":\"123456\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        assertEquals("apple@apple.com", userEmailRepository.findAllByEmailSent(false).get(0).getUser().getEmail());
    }

    @Test
    public void FindRegisteredUserDetailTest() throws Exception {

        mockMvc.perform(post("/registration")
                .content("{\"email\":\"apple@apple.com\",\"password\":\"123456\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        assertEquals("apple@apple.com" ,userDetailRepository.findByUser(userRepository.findByEmail("apple@apple.com")).getFirstName());
    }
}
