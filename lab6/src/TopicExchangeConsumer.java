import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class TopicExchangeConsumer extends Thread {
    private final String key;

    public TopicExchangeConsumer(String key) {
        this.key = key;
    }

    @Override
    public void run() {
        try {

            // connection & channel
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // exchange
            String exchangeName = "space";
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);

            // queue & bind
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchangeName, key);

            // consumer (message handling)
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    String message = new String(body, StandardCharsets.UTF_8);
                    System.out.println("\n[received: \"" + message + "\"]");
                }
            };

            channel.basicConsume(queueName, true, consumer);
        }
        catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
