package com.iscas.workingdiary.service.impl;

import com.alibaba.fastjson.JSON;
import com.client.Client;
import com.client.RepChainClient;
import com.iscas.workingdiary.config.ConstantProperties;
import com.iscas.workingdiary.service.RepClient;
import com.iscas.workingdiary.util.repchain.CustomRepChainClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

@Service()
public class RepClientImpl implements RepClient {
    @Autowired
    private ConstantProperties properties;
    private final String chaincodeId = properties.getChaincodeId();
    private final String host = properties.getRepchainHost();
    private final String jks = "jks/mykeystore_1.jks";
    private final String alias = "1";
    private final String jkspwd = "123";

    @Override
    public RepChainClient getRepClient() {
        return  new RepChainClient(host,jks,jkspwd, alias);
    }

    @Override
    public CustomRepChainClient getCustomRepClient(Certificate cert, PrivateKey privateKey) {
        return new CustomRepChainClient(this.host, cert, privateKey);
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
