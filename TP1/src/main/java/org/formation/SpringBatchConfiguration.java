package org.formation;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class SpringBatchConfiguration {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	JobRegistry jobRegistry;

	@Autowired
	JobExplorer jobExplorer;
	
	@Autowired
	JobRepository jobRepository;
	
	@Bean
	@DependsOn({ "jobRepository", "jobExplorer", "jobRegistry", "jobLauncher" })
	public SimpleJobOperator jobOperator() {
	    SimpleJobOperator jobOperator = new SimpleJobOperator();
	    try {
	        jobOperator.setJobExplorer(jobExplorer);
	    } catch (Exception e) {
	        throw new BeanCreationException("Could not create BatchJobOperator", e);
	    }
	    jobOperator.setJobLauncher(jobLauncher);
	    jobOperator.setJobRegistry(this.jobRegistry);
	    try {
	        jobOperator.setJobRepository(jobRepository);
	    } catch (Exception e) {
	        throw new BeanCreationException("Could not create BatchJobOperator", e);
	    }
	    return jobOperator;
	}
	@Bean
	public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
	  JobRegistryBeanPostProcessor postProcessor =
	    new JobRegistryBeanPostProcessor();
	  postProcessor.setJobRegistry(jobRegistry);

	  return postProcessor;
	}
}
