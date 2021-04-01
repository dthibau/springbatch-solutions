package org.formation;

import javax.annotation.Resource;

import org.formation.dummy.DummyJobListener;
import org.formation.dummy.DummyRecord;
import org.formation.model.InputProduct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfiguration {

	@Resource 
	FlatFileItemReader<InputProduct> productReader;
	@Resource 
	FlatFileItemWriter<InputProduct> productWriter;
	
	@Autowired
	JobBuilderFactory jobBuilderFactory; 
	
	@Autowired
	StepBuilderFactory stepBuilderFactory; 
	
	@Bean
	public Job flatFileJob() {
		return this.jobBuilderFactory.get("FlatFileJob")
		  .start(firstStep())
		  .build();
	}
	
	
	@Bean
	public Step firstStep() {
		return this.stepBuilderFactory.get("FlatFileStep")
	    .<InputProduct, InputProduct>chunk(10)
	    .reader(productReader)
	    .writer(productWriter)
	    .build();

	}
	
	
}
