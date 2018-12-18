package com.iscas.workingdiary.service.impl;

import com.alibaba.fastjson.JSON;
import com.client.RepChainClient;
import com.iscas.workingdiary.service.RepClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service()
public class RepClientImpl implements RepClient {
    // 开发阶段方便测试，暂时使用固定的jks和chaincodeId，后续考虑其他解决方法
    private final String chaincodeId = "ba9aa936765c3f2582387ed53684665e8307613741f7ed82e74ef2233a466e35";
    private final String host = "192.168.21.14:8081";
    private final String jks = "jks/mykeystore_1.jks";
    private final String alias = "1";

    /*private final String jks = "userjks/daiyongbing.jks";
    private final String alias = "daiyongbing";*/

   /*private final String jks = "userjks/hujing.jks";
    private final String alias = "hujing";*/
    private final String jkspwd = "123";

    @Override
    public RepChainClient getRepClient() {
        return  new RepChainClient(host,jks,jkspwd, alias);
    }

    public String getChaincodeId() {
        return this.chaincodeId;
    }

    @Override
    public List getParamList(Object o) {
        String jsonStr = JSON.toJSONString(o);
        List<String> argsList = new ArrayList<>();
        argsList.add(jsonStr);
        return argsList;
    }
}
