package info.m2sj.tistorybatch;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

/**
 * <pre>
 *  설명  :
 *
 *  작성일: 2022/09/29
 *  작성자: Rick
 *
 *  </pre>
 */
public class UniqueRunIdIncrementer extends RunIdIncrementer {

    private static final String RUN_ID = "run.id";

    @Override
    public JobParameters getNext(final JobParameters parameters) {
        JobParameters params = (parameters == null) ? new JobParameters() : parameters;
        return new JobParametersBuilder()
            .addLong(RUN_ID, params.getLong(RUN_ID, Long.valueOf(0)) + 1)
            .toJobParameters();
    }
}
