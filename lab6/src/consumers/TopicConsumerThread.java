package consumers;

import com.rabbitmq.client.*;
import utils.ChannelCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class TopicConsumerThread extends Thread {
    private final String key;

    public TopicConsumerThread(String key) {
        this.key = key;
    }

    @Override
    public void run() {
        try {
            ChannelCreator channelCreator = new ChannelCreator();
            channelCreator.addTopicExchange();
            channelCreator.addTopicExchangeQueue(key);
            Channel channel = channelCreator.getChannel();

            // consumer (message handling)
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    String message = new String(body, StandardCharsets.UTF_8);
                    System.out.println("\n[received: \"" + message + "\"]");
                }
            };

            channel.basicConsume(channelCreator.getQueueName(), true, consumer);
        }
        catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
