import com.rabbitmq.client.Channel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class Administrator {
    public static void main(String[] args) throws IOException, TimeoutException {
        new Thread(new TopicExchangeConsumer("admin")).start();

        Set<String> targets = new HashSet<>();
        targets.add("agencies");
        targets.add("transporters");
        targets.add("all");

        ChannelCreator channelCreator = new ChannelCreator();
        channelCreator.addTopicExchange();
        Channel channel = channelCreator.getChannel();

        System.out.println("ADMINISTRATOR");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("Select target (agencies/transporters/all): ");
            String target = reader.readLine();

            if (targets.contains(target)) {
                System.out.print("Message: ");
                String message = "ADMIN#" + reader.readLine();
                try {
                    channel.basicPublish("space", "admin." + target, null, message.getBytes(StandardCharsets.UTF_8));
                    System.out.println("[sent \"" + message + "\" to \"admin." + target + "\"]");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Incorrect target (\"" + target + "\").");
            }
        }
    }
}
