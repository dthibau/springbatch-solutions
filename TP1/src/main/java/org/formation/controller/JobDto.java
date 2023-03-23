package org.formation.controller;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;

public class JobDto {

	private String name;
	private JobInstance lastJobInstance;
	private JobExecution lastExecution;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public JobExecution getLastExecution() {
		return lastExecution;
	}
	public void setLastExecution(JobExecution lastExecution) {
		this.lastExecution = lastExecution;
	}
	public JobInstance getLastJobInstance() {
		return lastJobInstance;
	}
	public void setLastJobInstance(JobInstance lastJobInstance) {
		this.lastJobInstance = lastJobInstance;
	}
	
}
