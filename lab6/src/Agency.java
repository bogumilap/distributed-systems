import com.rabbitmq.client.Channel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class Agency {
    public static void main(String[] args) throws IOException, TimeoutException {
        // info
        System.out.println("AGENCY");

        // setting agency's name - with job's ID used to identify jobs
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter agency's name: ");
        String agencyName = reader.readLine();

        // communication with transporters
        new Thread(new DefaultConsumerThread("space.agencies." + agencyName, null)).start();

        // communication with admin
        new Thread(new TopicExchangeConsumer("admin.agencies")).start();
        new Thread(new TopicExchangeConsumer("admin.all")).start();

        ChannelCreator channelCreator = new ChannelCreator();
        channelCreator.addTopicExchange();
        Channel channel = channelCreator.getChannel();

        Map<JobType, Channel> jobTypeChannelMap = getJobChannels();

        int jobID = 0;
        JobTypeUtils jobTypeUtils = new JobTypeUtils();
        while (true) {
            // creating jobs
            System.out.print("\nSelect job type (" + jobTypeUtils.getOptions() + "): ");
            String type = reader.readLine();

            if (jobTypeUtils.isValidType(type)) {
                JobType jobType = JobType.valueOf(type.toUpperCase());
                jobID++;
                Channel jobChannel = jobTypeChannelMap.get(jobType);
                String message = agencyName + "#" + jobID;
                // producer (publish msg) - just as in Z1_producer
                jobChannel.basicPublish("", "space.transporters." + jobType, null, message.getBytes());
                channel.basicPublish("space", "admin", null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("[sent job request #" + jobID + "]");
            } else {
                System.out.println("Incorrect job type (\"" + type + "\").");
            }
        }
    }

    public static Map<JobType, Channel> getJobChannels() throws IOException, TimeoutException {
        Map<JobType, Channel> jobTypeChannelMap = new HashMap<>();
        for (JobType jobType : JobType.values()) {
            ChannelCreator channelCreator = new ChannelCreator();
            channelCreator.addQueue("space.transporters." + jobType);
            jobTypeChannelMap.put(jobType, channelCreator.getChannel());
        }
        return jobTypeChannelMap;
    }
}
