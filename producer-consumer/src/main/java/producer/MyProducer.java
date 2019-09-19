package producer;

import entity.TestMqBean;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MyProducer {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection;
        Session session;
        Destination destination;
        MessageProducer messageProducer;
        MessageConsumer messageConsumer;

        connectionFactory = new ActiveMQConnectionFactory("admin","admin","tcp://127.0.0.1:61616");
        try{
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);
            Destination destination_request = session.createQueue("request-queue");
            Destination destination_response = session.createQueue("response-queue");
            messageProducer = session.createProducer(destination_request);
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            messageConsumer = session.createConsumer(destination_response);
            TestMqBean bean = new TestMqBean();
            bean.setAge(23);

            for (int i = 0; i < 10; i++) {
                System.out.println("send message - " + i);
                messageProducer.send(session.createObjectMessage(bean));
            }
            messageProducer.close();
            System.out.println("消息发送成功...");

            messageConsumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    try {
                        if (null != message){
                            TextMessage textMessage = (TextMessage) message;
                            System.out.println("收到回馈消息：" + textMessage.getText());
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
