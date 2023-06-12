package utils;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChannelCreator {
    private final Channel channel;
    private String queueName = null;
    private final String exchangeName = "admin";

    public ChannelCreator() throws IOException, TimeoutException {
        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void addTopicExchange() throws IOException {
        // exchange
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
    }

    public void addTopicExchangeQueue(String key) throws IOException {
        queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchangeName, key);
    }

    public void addQueue(String queueName) throws IOException {
        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicQos(1);
    }

    public Channel getChannel() {
        return channel;
    }

    public String getQueueName() {
        return queueName;
    }
}
