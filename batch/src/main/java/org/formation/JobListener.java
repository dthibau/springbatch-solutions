package org.formation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobListener implements JobExecutionListener {

    Logger logger = LoggerFactory.getLogger(JobListener.class);
    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info(jobExecution.getJobInstance().getJobName() + "started  context is " + jobExecution.getExecutionContext());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        logger.info(jobExecution.getJobInstance().getJobName() + "finished  context is " + jobExecution.getExecutionContext());
    }
}
