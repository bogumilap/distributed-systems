import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class TopicExchangeProducer {
    private final String exchangeName = "space";
    private final Channel channel;

    public TopicExchangeProducer() throws IOException, TimeoutException {
        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        this.channel = connection.createChannel();

        // exchange
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
    }

    public void send(String message, String key) throws IOException {
        // publish
        channel.basicPublish(exchangeName, key, null, message.getBytes(StandardCharsets.UTF_8));
    }
}
