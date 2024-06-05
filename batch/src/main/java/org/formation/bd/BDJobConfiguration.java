package org.formation.bd;

import jakarta.annotation.Resource;
import org.formation.model.InputProduct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;


import javax.sql.DataSource;

@Configuration
public class BDJobConfiguration {



	@Autowired
	FournisseurPartitioner fournisseurPartitioner;
	
	@Resource
	ItemReader<InputProduct> jdbcProductReader;
	@Resource
	ItemWriter<InputProduct> jdbcProductWriter;

	@Autowired
	JobRepository jobRepository;

	@Autowired
	PlatformTransactionManager platformTransactionManager;


	@Bean
	public Job bdJob() throws Exception {

		return new JobBuilder("BDProductJob", jobRepository).start(masterStep()).build();

	}

	@Bean
    public Step masterStep() throws Exception 
    {
        return new StepBuilder("masterStep", jobRepository )
                .partitioner(slaveStep().getName(), fournisseurPartitioner)
                .step(slaveStep())
                .gridSize(3)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }
	
	@Bean
    public Step slaveStep() throws Exception 
    {
        return new StepBuilder("slaveStep", jobRepository )
                .<InputProduct, InputProduct>chunk(100, platformTransactionManager)
                .reader(jdbcProductReader)
                .writer(jdbcProductWriter)
                .build();
    }
	

	



}
