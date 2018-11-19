package com.iscas.workingdiary.service;

import com.alibaba.fastjson.JSON;
import com.client.RepChainClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service()
public class RepClientImpl implements RepClient {
    // 开发阶段方便测试，暂时使用固定的jks和chaincodeId，后续考虑其他解决方法
    private final String chaincodeId = "f67952e094ddc4a7baf10a35d40dd6c462beafacdb71a2c5a0a2ed430fc109d9";
    private final String host = "192.168.21.14:8081";
    private final String jks = "jks/mykeystore_1.jks";
    private final String jkspwd = "123";
    private final String alias = "1";
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
