package rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author: DANGO
 * @date 2018/7/12 10:52
 * @Description:
 */
public class Send {

    private final static String QUEUE_NAME = "dango_testQueue1";

    public static void main(String[] argv) throws Exception {
        for (int i=0;i<15000;i++) {
            ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("121.43.172.54");
            factory.setHost("localhost");
            factory.setUsername("dango");
            factory.setPassword("dango");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");

            channel.close();
            connection.close();
        }
    }
}
