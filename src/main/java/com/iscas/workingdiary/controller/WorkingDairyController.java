package com.iscas.workingdiary.controller;

import com.client.RepChainClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscas.workingdiary.util.GetEnvProperties;
import com.iscas.workingdiary.util.RepChainUtils;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("user")
public class WorkingDairyController {

    @PostMapping("postWorkingDairy")
    public void postWorkingDairyWithSign(HttpServletRequest request){
        GetEnvProperties envProperties = new GetEnvProperties();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = "{}";
        try {
            jsonStr = objectMapper.writeValueAsString(request.getParameterMap());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(jsonStr);
        List<String> argsList = new ArrayList<>();
        argsList.add(jsonStr);

        /*String host = envProperties.getProperty("repchain.repchainHost");
        String port = envProperties.getProperty("repchain.repchainPort");
        String chaincodeId = envProperties.getProperty("repchain.chaincodeId");*/
        String chaincodeId = "9a5eaf3a61e92e3bd47dfd69bb813cd6263af2dae9d1a4a3adcaadf57926f2d2";
        String url = "192.168.21.14:8081/test";
        String hexTransaction = RepChainUtils.createHexTransaction(chaincodeId, "workingDiaryProof", argsList);
        System.out.println("HexTransaction:"+hexTransaction);
        RepChainClient.doPost(url, hexTransaction);
    }
}
