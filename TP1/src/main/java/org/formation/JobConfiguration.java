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
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfiguration {

	@Resource 
	ItemReader<InputProduct> productReader;
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
		  .next(secondStep())
		  .next(thirdStep())
		  .build();
	}
	
	
	@Bean
	public Step firstStep() {
		return this.stepBuilderFactory.get("SkipStep")
	    .<InputProduct, OutputProduct>chunk(10)
	    .reader(productReader)
	    .processor(productProcessors)
	    .listener(productProcessor)
	    .writer(productXmlWriter)
	    .faultTolerant()
	    .skipLimit(200)
	    .skip(ValidationException.class)
	    .build();

	}
	@Bean
	public Step secondStep() {
		return this.stepBuilderFactory.get("SkipStepAllowStart")
	    .<InputProduct, OutputProduct>chunk(10)
	    .reader(productReader)
	    .processor(productProcessors)
	    .listener(productProcessor)
	    .writer(productXmlWriter)
	    .faultTolerant()
	    .skipLimit(200)
	    .skip(ValidationException.class)
	    .allowStartIfComplete(true)
	    .build();

	}
	
	@Bean
	public Step thirdStep() {
		return this.stepBuilderFactory.get("thirdStep")
	    .<InputProduct, OutputProduct>chunk(10)
	    .reader(productReader)
	    .processor(productProcessors)
	    .listener(productProcessor)
	    .writer(productXmlWriter)
	    .startLimit(2)
	    .build();

	}
	

	
}
