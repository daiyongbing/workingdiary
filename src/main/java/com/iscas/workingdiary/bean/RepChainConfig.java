package com.iscas.workingdiary.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RepChain配置类
 */

public class RepChainConfig {

    private String repchainHost;
    private String repchainPort;
    private String chaincodeId;

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

    public String getChaincodeId() {
        return chaincodeId;
    }

    public void setChaincodeId(String chaincodeId) {
        this.chaincodeId = chaincodeId;
    }
}
