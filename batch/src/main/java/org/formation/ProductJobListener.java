package org.formation;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductJobListener {

    @Value("${application.input-directory}")
    private String inputDirectory;
    @Value("${application.temp-directory}")
    private String tempDirectory;
    @Value("${application.output-directory}")
    private String outputDirectory;


    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        jobContext.put("input.directory", inputDirectory);
        jobContext.put("temp.directory", tempDirectory);
        jobContext.put("output.directory", outputDirectory);

        System.out.println("Job Context " + jobContext);

    }

}
