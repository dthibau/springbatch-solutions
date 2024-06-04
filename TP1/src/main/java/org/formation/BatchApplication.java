package org.formation;

import jakarta.annotation.Resource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.JobExecution;

@SpringBootApplication
public class BatchApplication  implements CommandLineRunner {

	@Autowired
	JobLauncher jobLauncher;

	@Resource
	Job fileJob;

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(BatchApplication.class, args);

/*		Arrays.asList(context.getBeanDefinitionNames()).stream()
				.filter(n -> n.toLowerCase().contains("job") || n.toLowerCase().contains("step") || n.toLowerCase().contains("batch") )
				.forEach(System.out::println);*/


	}

	@Override
	public void run(String... args) throws Exception {

		Map<String, JobParameter<?>> parametersMap = new HashMap<>();
		parametersMap.put("JOUR", new JobParameter(args[0], String.class,true));

		JobExecution jobExecution = jobLauncher.run(fileJob, new JobParameters(parametersMap));


		System.out.println(jobExecution);
	}
}
