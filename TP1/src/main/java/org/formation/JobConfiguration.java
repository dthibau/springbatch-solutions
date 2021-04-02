package org.formation;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfiguration {

	@Resource 
	ItemReader<InputProduct> jsonProductReader;
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
		return this.jobBuilderFactory.get("JsonXMLJob")
		  .start(firstStep())
		  .build();
	}
	
	
	@Bean
	public Step firstStep() {
		return this.stepBuilderFactory.get("JsonXmlStep")
	    .<InputProduct, OutputProduct>chunk(10)
	    .reader(jsonProductReader)
	    .processor(productProcessors)
	    .listener(productProcessor)
	    .writer(productXmlWriter)
	    .build();

	}
	

	
}
