package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.client.RepChainClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscas.workingdiary.util.Byte2Hex;
import com.iscas.workingdiary.util.GetEnvProperties;
import com.iscas.workingdiary.util.HttpClient;
import com.iscas.workingdiary.util.RepChainUtils;
import com.protos.Peer;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/diary")
public class WorkingDairyController {

    @PostMapping(value = "postWorkingDiary", produces = "application/json;charset=UTF-8")
    public JSONObject postWorkingDairyWithSign(@RequestBody JSONObject jsonParam){

        String jsonStr = JSON.toJSONString(jsonParam);
        System.out.println(jsonStr);
        List<String> argsList = new ArrayList<>();
        argsList.add(jsonStr);

        String chaincodeId = "43a60b17f71e80bbd3e7422ba007716bcbce884a827b636dd891d5f5c2bd5848";
        String host = "192.168.21.14:8081";
        String funcName = "workingDiaryProof";
        String jks = "jks/mykeystore_1.jks";
        String jkspwd = "123";
        String alias = "1";
        RepChainClient repChainClient = new RepChainClient(host,jks,jkspwd, alias);
        String hexTransaction = RepChainUtils.createHexTransaction(repChainClient, chaincodeId, "workingDiaryProof", argsList);
        System.out.println("HexTransaction:"+hexTransaction);
        return repChainClient.postTranByString("\""+hexTransaction+"\"");
    }

    @GetMapping(value = "queryWorkingDiary")
    public JSONObject queryWorkingDairy(@RequestParam("id") String userName){
        List<String> argsList = new ArrayList<>();
        System.out.println(userName);
        argsList.add(userName);

        String chaincodeId = "43a60b17f71e80bbd3e7422ba007716bcbce884a827b636dd891d5f5c2bd5848";
        String host = "192.168.21.14:8081";
        String funcName = "queryWorkingDairy";
        String jks = "jks/mykeystore_1.jks";
        String jkspwd = "123";
        String alias = "1";
        RepChainClient repChainClient = new RepChainClient(host,jks,jkspwd, alias);
        String hexTransaction = RepChainUtils.createHexTransaction(repChainClient, chaincodeId, "queryWorkingDiary", argsList);
        System.out.println("HexTransaction:"+hexTransaction);
        return repChainClient.postTranByString("\""+hexTransaction+"\"");
    }
}
