package com.iscas.workingdiary.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class GetEnvProperties {
    @Autowired
    private Environment env;

    public String getProperty(String param){
        return env.getProperty(param);
    }
}
