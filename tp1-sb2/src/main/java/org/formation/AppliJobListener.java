package org.formation;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.item.ExecutionContext;


public class AppliJobListener  {

	private final String inputDirectory;
	private final String tempDirectory;
	private final String outputDirectory;

	public AppliJobListener(String inputDirectory, String tempDirectory, String outputDirectory) {
		super();
		this.inputDirectory = inputDirectory;
		this.tempDirectory = tempDirectory;
		this.outputDirectory = outputDirectory;
	}
	

	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {
		ExecutionContext jobContext = jobExecution.getExecutionContext();
		jobContext.put("input.directory", inputDirectory);
		jobContext.put("temp.directory", tempDirectory);
		jobContext.put("output.directory", outputDirectory);
		
	    System.out.println("Job Context "+ jobContext);

	}

	

}
