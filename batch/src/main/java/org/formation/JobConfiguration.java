package org.formation;

import jakarta.annotation.Resource;

import org.formation.model.InputProduct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.formation.model.OutputProduct;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfiguration {

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
	JobRepository jobRepository;

	@Autowired
	PlatformTransactionManager transactionManager;
	


	@Bean
	Job fileJob() {
			return new JobBuilder("stepConfigJob", jobRepository)
					.start(step1())
					.next(step2())
					.next(step3())
					.listener(new JobListener())
					.build();
	}

	@Bean
	public Step step1() {
		return new StepBuilder("step1", jobRepository)
				.<InputProduct,OutputProduct>chunk(10, transactionManager)
				.reader(productReader)
				.processor(productProcessors)
				.writer(xmlProductWriter)
				.faultTolerant()
				.skipLimit(200)
				.skip(ValidationException.class)
				.build();
	}

	@Bean
	public Step step2() {
		return new StepBuilder("step2", jobRepository)
				.<InputProduct,OutputProduct>chunk(10, transactionManager)
				.allowStartIfComplete(true)
				.reader(productReader)
				.processor(productProcessors)
				.writer(xmlProductWriter)
				.faultTolerant()
				.skipLimit(200)
				.skip(ValidationException.class)
				.build();
	}

	@Bean
	public Step step3() {
		return new StepBuilder("step3", jobRepository)
				.<InputProduct,OutputProduct>chunk(10, transactionManager)
				.startLimit(2)
				.reader(productReader)
				.processor(productProcessors)
				.writer(xmlProductWriter)
				.build();
	}
}
