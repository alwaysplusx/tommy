package com.harmony.tommy.jms;

import javax.jms.JMSException;

public interface MessageHandler {

    /**
     * 发送一个文本消息
     * 
     * @param message
     * @throws JMSException
     */
    public void sendMessage(String message) throws JMSException;

    /**
     * 接收消息
     * 
     * @return
     * @throws JMSException
     */
    public Object receiveMessage() throws JMSException;

}
