package com.iscas.workingdiary.service.impl;

import com.iscas.workingdiary.service.MsgProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.Queue;

@Service
public class MsgProducerServiceImpl implements MsgProducerService {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    @Override
    public void sendMessage(final String msg) {
        this.jmsMessagingTemplate.convertAndSend(this.queue, msg);
    }

    @Override
    public void sendMessage(Destination destination, final String msg) {
        this.jmsMessagingTemplate.convertAndSend(destination, msg);
    }
}
