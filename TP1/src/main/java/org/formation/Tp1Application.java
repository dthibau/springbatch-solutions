package org.formation;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableBatchProcessing
public class Tp1Application implements CommandLineRunner {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;

	@Value("${appli.job.id}")
	long id;
	
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Tp1Application.class, args);

//		Arrays.asList(context.getBeanDefinitionNames()).stream()
//			 .filter(n -> n.toLowerCase().contains("job") || n.toLowerCase().contains("step") || n.toLowerCase().contains("batch") )
//		     .forEach(System.out::println);

	}

	@Override
	public void run(String... args) throws Exception {

		Map<String, JobParameter> parametersMap = new HashMap<>();
		parametersMap.put("DATE", new JobParameter(new Date(),false));
		parametersMap.put("ID", new JobParameter(id));

		JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parametersMap));

		System.out.println("Job done " + jobExecution);
	}

}
