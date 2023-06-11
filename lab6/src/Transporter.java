import consumers.DefaultConsumerThread;
import consumers.TopicConsumerThread;
import utils.JobType;
import utils.JobTypeUtils;

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
        JobType jobType1 = selectJobType(1, jobTypeUtils, reader, null);
        JobType jobType2 = selectJobType(2, jobTypeUtils, reader, jobType1);

        System.out.println("Waiting for job orders...");

        new Thread(new DefaultConsumerThread("space.transporters." + jobType1, transporterID)).start();
        new Thread(new DefaultConsumerThread("space.transporters." + jobType2, transporterID)).start();
        new Thread(new TopicConsumerThread("admin.transporters")).start();
        new Thread(new TopicConsumerThread("admin.all")).start();
    }

    public static JobType selectJobType(int ID, JobTypeUtils jobTypeUtils, BufferedReader reader, JobType existingJobType) throws IOException {
        JobType jobType = null;
        while (jobType == null) {
            System.out.print("Select supported job type #" + ID + " (" + jobTypeUtils.getOptions() + "): ");
            String input = reader.readLine();
            if (jobTypeUtils.isValidType(input)) {
                JobType selectedJobType = JobType.valueOf(input.toUpperCase());
                if (selectedJobType == existingJobType) {
                    System.out.println("Job #2 has to be different than job #1");
                } else {
                    jobType = selectedJobType;
                }
            } else {
                System.out.println("Incorrect job type (\"" + input + "\").");
            }
        }
        return jobType;
    }
}
