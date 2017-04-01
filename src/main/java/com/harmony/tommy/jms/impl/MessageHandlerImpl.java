package com.harmony.tommy.jms.impl;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.harmony.tommy.jms.MessageHandler;

@Stateless
public class MessageHandlerImpl implements MessageHandler {

    @Resource
    private ConnectionFactory connectionFactory;
    @Resource
    private Destination destination;

    @Override
    public void sendMessage(String message) throws JMSException {
        Connection conn = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            // 创建一个连接
            conn = connectionFactory.createConnection();
            conn.start();
            // 由连接创建会话
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 创建生产者
            producer = session.createProducer(destination);
            // 设置是否持久化
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // 由会话创建一个消息
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(message);
            // 生产者生产消息
            producer.send(textMessage);
        } finally {
            producer.close();
            session.close();
            conn.close();
        }
    }

    @Override
    public Object receiveMessage() throws JMSException {
        Connection conn = null;
        Session session = null;
        MessageConsumer consumer = null;
        try {
            conn = connectionFactory.createConnection();
            conn.start();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(destination);
            Message message = consumer.receive();
            if (message instanceof TextMessage) {
                return ((TextMessage) message).getText();
            }
            if (message instanceof ObjectMessage) {
                return ((ObjectMessage) message).getObject();
            }
            return message;
        } finally {
            consumer.close();
            session.close();
            conn.close();
        }
    }

}
