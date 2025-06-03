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
			return new JobBuilder("bdToFlatJob", jobRepository)
					.start(bdToFlatStep())
					.listener(new JobListener())
					.build();
	}

	@Bean
	public Step bdToFlatStep() {
		return new StepBuilder("bdToFlatStep", jobRepository)
				.<InputProduct,OutputProduct>chunk(10, transactionManager)
				.reader(jdbcProductReader)
				.processor(productProcessors)
				.writer(flatFileproductWriter)
				.build();
	}
}
