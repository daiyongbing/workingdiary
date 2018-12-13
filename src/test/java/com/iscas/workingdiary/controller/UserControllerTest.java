package com.iscas.workingdiary.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }


    @Test
    public void register() {
    }

    @Test
    public void checkUserName() {
    }

    @Test
    public void checkUserId() {
    }

    @Test
    public void updateUserByName() {
    }

    @Test
    public void deleteUserById() {
    }

    @Test
    public void logOut() {
    }

    @Test
    public void logOff() {
    }
}