package com.iscas.workingdiary;

import com.github.pagehelper.PageHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Properties;

@SpringBootApplication
@MapperScan("com.iscas.workingdiary.mapper")
@EnableSwagger2
public class Application {

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 分页不生效？在properties中配置却可以，有时间再研究吧
     * @param args
     */
    /*@Bean
    public PageHelper pageHelper(){
         PageHelper pageHelper = new PageHelper();
         Properties properties = new Properties();
         properties.setProperty("offsetAsPageNum","true");
         properties.setProperty("rowBoundsWithCount","true");
         properties.setProperty("reasonable","true");
         properties.setProperty("dialect","mysql");
         pageHelper.setProperties(properties);
         return pageHelper;
    }*/

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}