package org.formation;

import jakarta.annotation.Resource;
import org.formation.model.InputProduct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.formation.model.OutputProduct;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfiguration {

	@Resource
	FlatFileItemReader<InputProduct> productReader;
	@Resource
	FlatFileItemWriter<OutputProduct> productWriter;

	@Resource
	ItemProcessor<InputProduct, OutputProduct> productProcessors;

	@Autowired
	JobRepository jobRepository;

	@Autowired
	PlatformTransactionManager transactionManager;



	@Bean
	Job fileJob() {
			return new JobBuilder("fileJob", jobRepository)
					.start(fileStep())
					.listener(new JobListener())
					.build();

	}

	@Bean
	public Step fileStep() {
		return new StepBuilder("fileStep", jobRepository)
				.<InputProduct,OutputProduct>chunk(10, transactionManager)
				.reader(productReader)
				.processor(productProcessors)
				.writer(productWriter)
				.build();
	}


}
