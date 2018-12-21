package com.iscas.workingdiary.service;

import com.client.Client;
import com.client.RepChainClient;
import com.iscas.workingdiary.util.repchain.CustomRepChainClient;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.List;

@Service()
public interface RepClient {
    RepChainClient getRepClient();

    CustomRepChainClient getCustomRepClient(Certificate cert, PrivateKey privateKey);

    String getChaincodeId();

    List getParamList(Object o);
}
