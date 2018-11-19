package com.iscas.workingdiary.service;

import com.client.RepChainClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service()
public interface RepClient {
    RepChainClient getRepClient();

    String getChaincodeId();

    List getParamList(Object o);
}
