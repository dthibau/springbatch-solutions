package org.formation;

import jakarta.annotation.Resource;

import org.formation.model.InputProduct;
import org.formation.io.CleanUpTasklet;
import org.formation.io.InitTasklet;
import org.formation.model.OutputProduct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfiguration {


	public static String CSV2CSV_STEP = "csv2csvStep";
	public static String JSON2CSV_STEP = "json2csvStep";
	public static String CSV2XML_STEP = "csv2xmlStep";
	public static String INIT_STEP = "initStep";
	public static String CLEANUP_STEP ="cleanUpStep";

	@Resource
	ItemReader<InputProduct> productReader;
	@Resource
	ItemReader<InputProduct> jsonProductReader;
	@Resource
	ItemReader<InputProduct> jdbcProductReader;

	@Resource
	ItemWriter<OutputProduct> flatFileproductWriter;
	@Resource
	ItemWriter<OutputProduct> xmlProductWriter;


	@Resource
	ItemProcessor<InputProduct, OutputProduct> productProcessors;

	@Autowired
	ProductStepListener productStepListener;

	@Autowired
	ProductJobListener productJobListener;


	@Autowired
	InitTasklet initTasklet;

	@Autowired
	CleanUpTasklet cleanUpTasklet;

	@Autowired
	JobRepository jobRepository;

	@Autowired
	PlatformTransactionManager transactionManager;
	


	@Bean
	Job fileJob() {
			return new JobBuilder("fileJob", jobRepository)
					.start(initStep())
					.next(csv2csvStep())
					.next(json2csvStep())
					.next(csv2xmlStep())
					.next(cleanUpStep())
					.listener(productJobListener)
					.build();
	}

	@Bean
	public Step initStep() {
			return new StepBuilder(INIT_STEP, jobRepository)
					    .tasklet(initTasklet, transactionManager)
					    .build();
			}

	@Bean
	public Step cleanUpStep() {
		return new StepBuilder(CLEANUP_STEP, jobRepository)
				.tasklet(cleanUpTasklet, transactionManager)
				.build();
	}

	@Bean
	public Step csv2csvStep() {
		return new StepBuilder(CSV2CSV_STEP, jobRepository)
				.<InputProduct,OutputProduct>chunk(10, transactionManager)
				.reader(productReader)
				.processor(productProcessors)
				.writer(flatFileproductWriter)
				.faultTolerant()
				.skipLimit(200)
				.skip(ValidationException.class)
				.listener(productStepListener)
				.build();
	}
	@Bean
	public Step json2csvStep() {
		return new StepBuilder(JSON2CSV_STEP, jobRepository)
				.<InputProduct,OutputProduct>chunk(10, transactionManager)
				.reader(jsonProductReader)
				.processor(productProcessors)
				.writer(flatFileproductWriter)
				.faultTolerant()
				.skipLimit(200)
				.skip(ValidationException.class)
				.listener(productStepListener)
				.build();
	}
	@Bean
	public Step csv2xmlStep() {
		return new StepBuilder(CSV2XML_STEP, jobRepository)
				.<InputProduct,OutputProduct>chunk(10, transactionManager)
				.reader(productReader)
				.processor(productProcessors)
				.writer(xmlProductWriter)
				.listener(productStepListener)
				.build();
	}


}
