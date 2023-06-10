import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JobTypeUtils {
    public JobTypeUtils() { }

    public String getOptions() {
        return Stream.of(JobType.values())
                .map(Object::toString)
                .map(String::toLowerCase)
                .collect(Collectors.joining("/"));
    }

    public boolean isValidType(String type) {
        for (JobType jobType : JobType.values()) {
            if (Objects.equals(jobType.toString(), type.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
