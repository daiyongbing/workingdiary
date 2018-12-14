package com.iscas.workingdiary.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CertControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void deleteCertByCertNo() {
    }

    @Test
    public void deleteCertByCertNo1() {
    }

    @Test
    public void queryCertByNo() {
    }

    @Test
    public void queryAllCert() {
    }

    @Test
    public void updateCert() {
    }

    @Test
    public void verifyCert() {
    }

    @Test
    public void generateCert() {
    }

    @Test
    public void uploadCert() throws Exception {
        mockMvc.perform(fileUpload("/cert/upload")
                .file(new MockMultipartFile("fileName", "RESTful API拦截顺序.jpg", "multipart/form-data","this is a test".getBytes()))
        ).andExpect(status().isOk());
    }

    @Test
    public void downloadCert() {
    }

    @Test
    public void signCertByAdmin() {
    }

    @Test
    public void signCert() {
    }

    @Test
    public void destroyCert() {
    }

    @Test
    public void replaceCert() {
    }
}