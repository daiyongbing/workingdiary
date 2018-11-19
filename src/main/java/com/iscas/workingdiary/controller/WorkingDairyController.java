package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.client.RepChainClient;
import com.iscas.workingdiary.service.RepClient;
import com.iscas.workingdiary.util.RepChainUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/diary")
public class WorkingDairyController {

    @Autowired
    private RepClient repClient;

    @PostMapping(value = "postWorkingDiary", produces = "application/json;charset=UTF-8")
    public JSONObject postWorkingDairyWithSign(@RequestBody JSONObject jsonParam){
        RepChainClient repChainClient = repClient.getRepClient();
        List<String> argsList = repClient.getParamList(jsonParam);
        String hexTransaction = RepChainUtils.createHexTransaction(repChainClient, repClient.getChaincodeId(),"workingDiaryProof", argsList);
        return repChainClient.postTranByString(hexTransaction);
    }

    @GetMapping(value = "queryWorkingDiary")
    public JSONObject queryWorkingDairy(@RequestParam("diaryKey")  String diaryKey){
        RepChainClient repChainClient = repClient.getRepClient();
        List<String> argsList = repClient.getParamList(diaryKey);
        String hexTransaction = RepChainUtils.createHexTransaction(repChainClient, repClient.getChaincodeId(),"queryWorkingDiary", argsList);
        return repChainClient.postTranByString(hexTransaction);
    }

    @GetMapping(value = "queryIntegral")
    public JSONObject queryIntegral(@RequestParam("userName") String userName){
        RepChainClient repChainClient = repClient.getRepClient();
        List<String> argsList = repClient.getParamList(userName);
        String hexTransaction = RepChainUtils.createHexTransaction(repChainClient, repClient.getChaincodeId(),"queryIntegral", argsList);
        return repChainClient.postTranByString(hexTransaction);
    }
}
