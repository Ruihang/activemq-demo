package subscriber;

import entity.TestMqBean;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MyPublisher {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection;
        Session session;
        Topic destination;
        MessageProducer messageProducer;
        MessageConsumer messageConsumer;

        connectionFactory = new ActiveMQConnectionFactory("admin","admin","tcp://127.0.0.1:61616");

        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic("example.A");
            messageProducer = session.createProducer(destination);
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

            for (int i = 0; i < 10; i++) {
                Message message = session.createTextMessage("ActiveMq 发送的消息 - " + i);
                System.out.println("发送消息：" + "ActiveMq 发送的消息 - " + i);
                messageProducer.send(message);
            }
            session.commit();
            System.out.println("发送消息成成功...");

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
