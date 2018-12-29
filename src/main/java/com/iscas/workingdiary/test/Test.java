package com.iscas.workingdiary.test;

import redis.clients.jedis.Jedis;

public class Test {

    public static void main(String[] args){
        Jedis jdis = new Jedis("192.168.100.174", 6379);
        jdis.set("name", "test");
        System.out.println(jdis.get("name"));
    }
}
