package org.formation;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductJobListener {

    @Value("${appli.input-directory}")
    private String inputDirectory;
    @Value("${appli.temp-directory}")
    private String tempDirectory;
    @Value("${appli.output-directory}")
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
