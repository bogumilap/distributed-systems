import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class Administrator {
    public static void main(String[] args) throws IOException, TimeoutException {
        new Thread(new TopicExchangeConsumer("admin")).start();

        Set<String> targets = new HashSet<>();
        targets.add("agencies");
        targets.add("transporters");
        targets.add("all");

        TopicExchangeProducer producer = new TopicExchangeProducer();

        System.out.println("ADMINISTRATOR");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("Select target (agencies/transporters/all): ");
            String target = reader.readLine();

            if (targets.contains(target)) {
                System.out.print("Message: ");
                String message = "ADMIN#" + reader.readLine();
                try {
                    producer.send(message, "admin." + target);
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
