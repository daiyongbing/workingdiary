package com.iscas.workingdiary.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void register() throws Exception {
        String content = "{\n" +
                "            \"userName\":\"test\",\n" +
                "            \"userId\":\"0\",\n" +
                "            \"password\":\"123\",\n" +
                "            \"roleId\":\"0\"\n" +
                "        }";
        mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
        ).andExpect(status().isOk());
    }

    @Test
    public void login() throws Exception {
        String content = "{\n" +
                "            \"userName\":\"test\",\n" +
                "            \"password\":\"123\",\n" +
                "        }";
        String token = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
        ).andExpect(status().isOk())
        .andReturn().getResponse().getHeader("Authorization");
        System.out.println("token:"+token);
    }

    @Test
    public void checkUserName() throws Exception{
        mockMvc.perform(get("/user/checkname").contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("userName","test1")
        ).andExpect(status().isOk());
    }

    @Test
    public void checkUserId() throws Exception{
        mockMvc.perform(get("/user/checkid").contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("userId","1")
        ).andExpect(status().isOk());
    }

    @Test
    public void updateUserByName() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer JHFAOkvl.agbuhgorg.afgjpgug");
        String content = "{\n" +
                "            \"userSex\":\"female\",\n" +
                "            \"projectTeam\":\"区块链实验室\",\n" +
                "            \"userPosition\":\"研发\",\n" +
                "            \"leader\":\"leader\"\n" +
                "        }";
        mockMvc.perform(post("/user/update").contentType(MediaType.APPLICATION_JSON_UTF8)
                .headers(headers)
                .content(content)
        ).andExpect(status().isOk());
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