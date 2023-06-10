import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class DefaultConsumerThread extends Thread {
    private final String queueName;
    private final String transporterID;

    public DefaultConsumerThread(String queueName, String transporterID) {
        this.queueName = queueName;
        this.transporterID = transporterID;
    }

    @Override
    public void run() {
        try {
            ChannelCreator channelCreator = new ChannelCreator();
            channelCreator.addQueue(queueName);
            Channel channel = channelCreator.getChannel();

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Channel channel1 = null;
                    try {
                        ChannelCreator channelCreator = new ChannelCreator();
                        channelCreator.addTopicExchange();
                        channel1 = channelCreator.getChannel();
                    } catch (TimeoutException e) {
                        throw new RuntimeException(e);
                    }

                    String message = new String(body, StandardCharsets.UTF_8);
                    System.out.println("\n[received: \"" + message + "\"]");

                    // if transporter received message, it replies to agency
                    if (transporterID != null) {
                        String[] split = message.split("#");
                        String senderName = split[0];
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        String response = transporterID + "#" + message;
                        try {
                            channel.basicPublish("", "space.agencies." + senderName, null, response.getBytes());
                            System.out.println("[sent: \"" + response + "\" to \"" + senderName + "\"]");
                            channel.basicPublish("space", "admin", null, message.getBytes(StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
