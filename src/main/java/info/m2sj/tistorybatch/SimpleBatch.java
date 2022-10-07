package info.m2sj.tistorybatch;

import java.util.concurrent.TimeUnit;
import javax.batch.api.chunk.ItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class SimpleBatch {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public SimpleBatch(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }


    @Bean
    public Job simpleJob() {
        return jobBuilderFactory
            .get("simple-job")
            .start(simpleStep())
            .incrementer(new DateIncrementer())
            .build();
    }

    @Bean
    public Step simpleStep() {
        return this.stepBuilderFactory
            .get("simple-step")
            .<CsvTestDto, CsvTestDto>chunk(100)
            .reader(fileItemReader())
            .writer(itemWriter())
            .build();
    }

    @Bean
    public FlatFileItemReader<CsvTestDto> fileItemReader() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(",");
        lineTokenizer.setNames("iYear", "industryAggregation", "industryCode", "industryName", "units",
            "variableCode",
            "variableName", "variableCategory", "iValue", "industryCode2");

        BeanWrapperFieldSetMapper fieldSetMapper = new BeanWrapperFieldSetMapper();
        fieldSetMapper.setTargetType(CsvTestDto.class);
        fieldSetMapper.setStrict(false);

        DefaultLineMapper defaultLineMapper = new DefaultLineMapper();
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return new FlatFileItemReaderBuilder()
            .name("fileItemReader")
            .resource(new ClassPathResource("big-data.csv"))
            .lineMapper(defaultLineMapper)
            .linesToSkip(1)
            .build();
    }


    @Bean
    public ItemWriter<CsvTestDto> itemWriter() {
        return items -> {
            for (CsvTestDto item : items) {
                System.out.println(item);
            }
        };
    }

}
