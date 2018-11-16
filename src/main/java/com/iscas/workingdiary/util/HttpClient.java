package com.iscas.workingdiary.util;

import org.springframework.http.*;

import org.springframework.web.client.RestTemplate;


public class HttpClient {

    public static String postJson(String url, HttpMethod method, String body){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);  // application/json

        HttpEntity<String> entity = new HttpEntity<String>(body, headers);

        //  执行HTTP请求
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }
}
