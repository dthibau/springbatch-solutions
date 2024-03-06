package org.formation.dummy;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class DummyJobListener implements JobExecutionListener {



	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Job is starting " + jobExecution);
		
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("After job " + jobExecution);
		
	}

}
