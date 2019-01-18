package com.iscas.workingdiary.service;

import javax.jms.Destination;


public interface MsgProducerService {
    /**
     * 使用默认消息队列
     * @param msg
     */
    void sendMessage(final String msg);

    /**
     * 指定消息队列
     * @param destination
     * @param msg
     */
    void sendMessage(Destination destination, final String msg);
}
