package org.formation;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class DummyListener implements JobExecutionListener  {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        JobExecutionListener.super.beforeJob(jobExecution);
        System.out.println(jobExecution);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        JobExecutionListener.super.afterJob(jobExecution);
        System.out.println(jobExecution);

    }
}
