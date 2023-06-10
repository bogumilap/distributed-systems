import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Transporter {
    public static void main(String[] args) throws IOException {
        // info
        System.out.println("TRANSPORTER");

        // setting transporter's ID
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter transporter ID: ");
        String transporterID = reader.readLine();

        // selecting two supported job types
        JobTypeUtils jobTypeUtils = new JobTypeUtils();
        JobType jobType1 = selectJobType(1, jobTypeUtils, reader);
        JobType jobType2 = selectJobType(2, jobTypeUtils, reader);

        new Thread(new DefaultConsumerThread("space.transporters." + jobType1, transporterID)).start();
        new Thread(new DefaultConsumerThread("space.transporters." + jobType2, transporterID)).start();
        new Thread(new TopicExchangeConsumer("admin.transporters")).start();
        new Thread(new TopicExchangeConsumer("admin.all")).start();
    }

    public static JobType selectJobType(int ID, JobTypeUtils jobTypeUtils, BufferedReader reader) throws IOException {
        JobType jobType = null;
        while (jobType == null) {
            System.out.print("Select supported job type #" + ID + " (" + jobTypeUtils.getOptions() + "): ");
            String input = reader.readLine();
            if (jobTypeUtils.isValidType(input)) {
                jobType = JobType.valueOf(input.toUpperCase());
            } else {
                System.out.println("Incorrect job type (\"" + input + "\").");
            }
        }
        return jobType;
    }
}
