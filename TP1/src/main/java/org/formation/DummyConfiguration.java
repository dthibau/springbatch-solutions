package org.formation;

import jakarta.annotation.Resource;
import org.formation.dummy.DummyReader;
import org.formation.dummy.DummyRecord;
import org.formation.dummy.DummyWriter;
import org.formation.model.InputProduct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DummyConfiguration {

	@Resource
	FlatFileItemReader<InputProduct> productReader;
	@Resource
	FlatFileItemWriter<InputProduct> productWriter;


	@Autowired
	JobRepository jobRepository;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Bean
	Job fileJob() {
			return new JobBuilder("fileJob", jobRepository)
					.start(fileStep())
					.listener(new DummyListener())
					.build();

	}

	@Bean
	public Step fileStep() {
		return new StepBuilder("fileStep", jobRepository)
				.<InputProduct,InputProduct>chunk(10, transactionManager)
				.reader(productReader)
				.writer(productWriter)
				.build();
	}


}
