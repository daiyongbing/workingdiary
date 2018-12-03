package com.iscas.workingdiary.config;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ConstantPropertiesTest {
    @Autowired
    ConstantProperties properties;

    @Test
    public void getChaincodeId(){
        System.out.println(properties.getChaincodeId());
    }
}