package com.iscas.workingdiary;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.iscas.workingdiary.mapper")
//@ConfigurationProperties(prefix = "repchain")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}