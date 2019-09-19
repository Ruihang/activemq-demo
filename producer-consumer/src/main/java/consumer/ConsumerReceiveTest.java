package consumer;

import entity.TestMqBean;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Date;

public class ConsumerReceiveTest {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection;
        final Session session;
        Destination destination;
        MessageConsumer messageConsumer;
        final MessageProducer messageProducer;
        connectionFactory = new ActiveMQConnectionFactory("admin","admin","tcp://127.0.0.1:61616");
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Destination destination_request = session.createQueue("request-queue");
            Destination destination_response = session.createQueue("response-queue");

            messageConsumer = session.createConsumer(destination_request);
            messageProducer = session.createProducer(destination_response);
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            messageConsumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    try {
                        TestMqBean bean = (TestMqBean) ((ObjectMessage)message).getObject();
                        System.out.println(bean);
                        if (null != message){
                            System.out.println("收到消息" + bean.getAge());
                            Message textMessage = session.createTextMessage("已经成功接收到消息，现在开始回复" + new Date().toString());
                            messageProducer.send(textMessage);
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
