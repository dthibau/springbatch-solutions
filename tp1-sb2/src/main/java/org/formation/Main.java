package org.formation;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		
		System.setProperty("ENVIRONMENT", "mysql");
		
		// Load context
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"/batch/bd-job-context.xml");

		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("bdJob");
		Map<String, JobParameter> parametersMap = new HashMap<>();
		parametersMap.put("DATE", new JobParameter(new Date()));

		JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parametersMap));

		System.out.println("Job done " + jobExecution);

	}

}
