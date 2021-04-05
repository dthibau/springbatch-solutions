package org.formation;

import javax.annotation.Resource;

import org.formation.file.CleanupTasklet;
import org.formation.file.InitTasklet;
import org.formation.model.InputProduct;
import org.formation.model.OutputProduct;
import org.formation.model.ProductProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfiguration {

	public static String INIT_STEP = "initStep";
	public static String CSV2CSV_STEP = "csv2csvStep";
	public static String JSON2CSV_STEP = "json2csvStep";
	public static String CSV2XML_STEP = "csv2xmlStep";
	public static String CLEANUP_STEP = "initStep";
	
	@Autowired
	AppliJobListener appliJobListener;
	@Autowired
	AppliStepListener appliStepListener;
	
	@Autowired
	InitTasklet initTasklet;
	@Autowired
	CleanupTasklet cleanupTasklet;
	@Resource 
	ItemReader<InputProduct> productReader;
	@Resource 
	ItemReader<InputProduct> jsonProductReader;
	@Resource
	ItemProcessor<InputProduct,OutputProduct> productProcessors;
	@Resource 
	ItemWriter<OutputProduct> productWriter;
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
		  .start(initStep())
		  .next(csv2csv())
		  .next(json2csv())
		  .next(csv2xml())
		  .next(cleanupStep())
		  .listener(appliJobListener)
		  .build();
	}
	@Bean
	public ExecutionContextPromotionListener promotionListener() {
	  ExecutionContextPromotionListener listener = new
	  ExecutionContextPromotionListener();
	  listener.setKeys(new String[] {"input.directory","temp.directory","output.directory"});
	  return listener;
	}
	@Bean
	public Step initStep() {
		return this.stepBuilderFactory.get(INIT_STEP)
	    .tasklet(initTasklet)
	    .listener(promotionListener())
	    .build();
	}
	@Bean
	public Step csv2csv() {
		return this.stepBuilderFactory.get(CSV2CSV_STEP)
	    .<InputProduct, OutputProduct>chunk(10)
	    .reader(productReader)
	    .processor(productProcessors)
	    .writer(productWriter)
	    .faultTolerant()
	    .skipLimit(200)
	    .skip(ValidationException.class)
	    .listener(appliStepListener)
	    .build();

	}
	@Bean
	public Step json2csv() {
		return this.stepBuilderFactory.get(JSON2CSV_STEP)
	    .<InputProduct, OutputProduct>chunk(10)
	    .reader(jsonProductReader)
	    .processor(productProcessors)
	    .writer(productWriter)
	    .faultTolerant()
	    .skipLimit(200)
	    .skip(ValidationException.class)
	    .listener(appliStepListener)
	    .build();

	}
	
	@Bean
	public Step csv2xml() {
		return this.stepBuilderFactory.get(CSV2XML_STEP)
	    .<InputProduct, OutputProduct>chunk(10)
	    .reader(productReader)
	    .writer(productXmlWriter)
	    .startLimit(2)
	    .listener(appliStepListener)
	    .build();

	}
	@Bean
	public Step cleanupStep() {
		return this.stepBuilderFactory.get(CLEANUP_STEP)
	    .tasklet(cleanupTasklet)
	    .listener(promotionListener())
	    .build();
	}
	
}
