package org.formation;

import org.formation.dummy.DummyListener;
import org.formation.dummy.DummyReader;
import org.formation.dummy.DummyRecord;
import org.formation.dummy.DummyWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfiguration {

	@Autowired
	DummyReader dummyReader;
	@Autowired
	DummyWriter dummyWriter;

	@Autowired
	JobRepository jobRepository;

	@Autowired
	PlatformTransactionManager transactionManager;
	

	// A compléter
	@Bean
	public Job dummyJob() {
		return new JobBuilder("dummyJob", jobRepository)
				.start(firstStep())
				.next(secondStep())
				.listener(new DummyListener())
				.build();
	}

	@Bean Step firstStep() {
		return new StepBuilder("firstStep",jobRepository)
				.<DummyRecord, DummyRecord>chunk(10,transactionManager)
				.reader(dummyReader)
				.writer(dummyWriter)
				.build();
	}

	@Bean Step secondStep() {
		return new StepBuilder("secondStep",jobRepository)
				.<DummyRecord, DummyRecord>chunk(10,transactionManager)
				.reader(dummyReader)
				.writer(dummyWriter)
				.build();
	}
}
