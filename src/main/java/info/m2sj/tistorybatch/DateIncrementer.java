package info.m2sj.tistorybatch;

import java.time.LocalDateTime;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

public class DateIncrementer implements JobParametersIncrementer {

    @Override
    public JobParameters getNext(final JobParameters parameters) {
        return new JobParametersBuilder(parameters)
            .addString("currentDate", LocalDateTime.now().toString())
            .toJobParameters();
    }
}
