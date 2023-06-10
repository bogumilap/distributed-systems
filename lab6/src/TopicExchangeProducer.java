import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class TopicExchangeProducer {
    private final Channel channel;

    public TopicExchangeProducer() throws IOException, TimeoutException {
        ChannelCreator channelCreator = new ChannelCreator();
        channelCreator.addTopicExchange();
        channel = channelCreator.getChannel();
    }

    public void send(String message, String key) throws IOException {
        // publish
        channel.basicPublish("space", key, null, message.getBytes(StandardCharsets.UTF_8));
    }
}
