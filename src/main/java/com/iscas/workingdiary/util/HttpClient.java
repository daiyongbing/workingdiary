package com.iscas.workingdiary.util;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletInputStream;


@Service
public class HttpClient {

    public String client(String url, HttpMethod method, ServletInputStream inputStream){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_XML);  // application/xml

        //HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        HttpEntity<ServletInputStream> requestEntity = new HttpEntity<>(inputStream);
        System.out.println(requestEntity.getHeaders());
        System.out.println(requestEntity.getBody().toString());
        //  执行HTTP请求
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return response.getBody();
    }
}
