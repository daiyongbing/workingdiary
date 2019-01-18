package com.iscas.workingdiary.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsConsumer {
    @JmsListener(destination = "common.queue")

    public void receiveQueue(String text) {

        System.out.println(text);

    }
}
