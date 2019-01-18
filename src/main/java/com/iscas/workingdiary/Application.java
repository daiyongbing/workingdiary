package com.iscas.workingdiary;

import com.github.pagehelper.PageHelper;
import org.apache.activemq.command.ActiveMQQueue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.jms.Queue;
import java.util.Properties;

@SpringBootApplication
@MapperScan("com.iscas.workingdiary.mapper")
@EnableJms
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

  /*  @Bean
    JedisConnectionFactory jedisConnectionFactory(){
        return new JedisConnectionFactory();
    }*/

  @Bean
  public Queue queue(){
      return new ActiveMQQueue("common.queue");
  }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}