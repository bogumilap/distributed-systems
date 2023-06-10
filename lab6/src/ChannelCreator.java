import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChannelCreator {
    private final Channel channel;
    private final String exchangeName = "space";

    public ChannelCreator() throws IOException, TimeoutException {
        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void addTopicExchange() throws IOException {
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
    }

    public void addTopicExchangeQueue(String key) throws IOException {
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchangeName, key);
    }

    public void addQueue(String queueName) throws IOException {
        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicQos(1);
    }

    public Channel getChannel() {
        return channel;
    }
}
