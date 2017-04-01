### Java Message Service Example
#### openejb.xml 详细配置位于openejb-core.jar /META-INF/org/apache/openejb/service-jar.xml
```xml
<!-- 配置ActiveMQResourceAdapter -->
<Resource id="jmsResourceAdapter" type="ActiveMQResourceAdapter">
	BrokerXmlConfig broker:(tcp://localhost:8082)?useJmx=false
      ServerUrl tcp://localhost:8082
</Resource>

<!-- 配置ConnectionFactory -->
<Resource id="connectionFactory" type="javax.jms.ConnectionFactory">
	ResourceAdapter jmsResourceAdapter
</Resource>

<!-- 配置Destination(Queue) -->
<Resource id="queue" type="javax.jms.Queue"/>
```
#### Produce Message

```java
public void produceMessage(String message) throws JMSException {
	Connection conn = null;
	Session session = null;
	MessageProducer producer = null;
	try {
		// 创建一个连接,connectonFactory为配置资源注入
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
```
#### Consume Message

```java
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
		if(message instanceof TextMessage){
			return ((TextMessage)message).getText();
		}
		if(message instanceof ObjectMessage){
			return ((ObjectMessage)message).getObject();
		}
		return message;
	} finally {
		consumer.close();
		session.close();
		conn.close();
	}
}
```
