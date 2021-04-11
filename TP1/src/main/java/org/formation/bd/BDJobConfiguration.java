package org.formation.bd;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.formation.bd.item.FournisseurPartitioner;
import org.formation.model.InputProduct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class BDJobConfiguration {

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;


	@Autowired
	FournisseurPartitioner fournisseurPartitioner;
	
	@Resource
	ItemReader<InputProduct> jdbcProductReader;
	@Resource
	ItemWriter<InputProduct> jdbcProductWriter;

	@Resource
	DataSource inputProductDataSource;

	@Bean
	public Job bdJob() throws Exception {

		return jobBuilderFactory.get("BDProductJob").start(masterStep()).build();

	}

	@Bean
    public Step masterStep() throws Exception 
    {
        return stepBuilderFactory.get("masterStep")
                .partitioner(slaveStep().getName(), fournisseurPartitioner)
                .step(slaveStep())
                .gridSize(3)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }
	
	@Bean
    public Step slaveStep() throws Exception 
    {
        return stepBuilderFactory.get("slaveStep")
                .<InputProduct, InputProduct>chunk(100)
                .reader(jdbcProductReader)
                .writer(jdbcProductWriter)
                .build();
    }
	

	



}
