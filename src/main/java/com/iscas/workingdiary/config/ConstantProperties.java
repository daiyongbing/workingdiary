package com.iscas.workingdiary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ConstantProperties {
    @Value("${repchain.chaincodeId}")
    private String chaincodeId;

    @Value("${repchain.host}")
    private String repchainHost;

    @Value("${repchain.port}")
    private String repchainPort;

    @Value("${cer.path}")
    private String certPath;

    @Value("${jks.path}")
    private String jksPath;

    public String getChaincodeId() {
        return chaincodeId;
    }

    public void setChaincodeId(String chaincodeId) {
        this.chaincodeId = chaincodeId;
    }

    public String getRepchainHost() {
        return repchainHost;
    }

    public void setRepchainHost(String repchainHost) {
        this.repchainHost = repchainHost;
    }

    public String getRepchainPort() {
        return repchainPort;
    }

    public void setRepchainPort(String repchainPort) {
        this.repchainPort = repchainPort;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getJksPath() {
        return jksPath;
    }

    public void setJksPath(String jksPath) {
        this.jksPath = jksPath;
    }

}
