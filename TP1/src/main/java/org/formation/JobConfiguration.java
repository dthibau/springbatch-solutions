package org.formation;

import javax.annotation.Resource;

import org.formation.model.InputProduct;
import org.formation.model.OutputProduct;
import org.formation.model.ProductProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfiguration {

	@Resource 
	ItemReader<InputProduct> jdbcProductReader;
	@Resource
	ItemProcessor<InputProduct,OutputProduct> productProcessors;
	@Resource 
	ItemWriter<OutputProduct> productXmlWriter;
	@Autowired
	ProductProcessor productProcessor;
	
	@Autowired
	JobBuilderFactory jobBuilderFactory; 
	
	@Autowired
	StepBuilderFactory stepBuilderFactory; 
	
	
	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("BDJob")
		  .start(firstStep())
		  .build();
	}
	
	
	@Bean
	public Step firstStep() {
		return this.stepBuilderFactory.get("BDStep")
	    .<InputProduct, OutputProduct>chunk(10)
	    .reader(jdbcProductReader)
	    .processor(productProcessors)
	    .listener(productProcessor)
	    .writer(productXmlWriter)
	    .build();

	}
	

	
}
