package org.formation;

import org.formation.dummy.DummyJobListener;
import org.formation.dummy.DummyReader;
import org.formation.dummy.DummyRecord;
import org.formation.dummy.DummyWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfiguration {

	@Autowired 
	DummyReader dummyReader;
	@Autowired 
	DummyWriter dummyWriter;
	
	@Autowired
	JobBuilderFactory jobBuilderFactory; 
	
	@Autowired
	StepBuilderFactory stepBuilderFactory; 
	
	@Bean
	public Job DummyJob() {
		return this.jobBuilderFactory.get("DummyJob")
		  .start(firstStep())
		  .next(secondStep())
		  .listener(dummyListener())
		  .build();
	}
	
	
	@Bean
	public Step firstStep() {
		return this.stepBuilderFactory.get("Dummy1Step")
	    .<DummyRecord, DummyRecord>chunk(10)
	    .reader(dummyReader)
	    .writer(dummyWriter)
	    .build();

	}
	
	@Bean
	public Step secondStep() {
		return this.stepBuilderFactory.get("Dummy2Step")
	    .<DummyRecord, DummyRecord>chunk(10)
	    .reader(dummyReader)
	    .writer(dummyWriter)
	    .build();

	}
	
	@Bean
	public JobExecutionListener dummyListener() {
		return new DummyJobListener();
	}
}
