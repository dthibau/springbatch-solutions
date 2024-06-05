package org.formation;

import jakarta.annotation.Resource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class BatchApplication implements CommandLineRunner {

	@Autowired
	JobLauncher jobLauncher;


	@Resource
	Job bdJob;

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(BatchApplication.class, args);

/*		Arrays.asList(context.getBeanDefinitionNames()).stream()
				.filter(n -> n.toLowerCase().contains("job") || n.toLowerCase().contains("step") || n.toLowerCase().contains("batch") )
				.forEach(System.out::println);*/


	}

	@Override
	public void run(String... args) throws Exception {

		if (args.length == 0 ) {
			System.err.println("Usage: java -jar batch-application.jar [<id-string>]");
			System.exit(2);
		}

		Map<String, JobParameter<?>> parametersMap = new HashMap<>();
		JobParameter jobParameter = new JobParameter(args[0], String.class,true);
		parametersMap.put("id", jobParameter);


		JobExecution jobExecution = jobLauncher.run(bdJob, new JobParameters(parametersMap));

		System.out.println("Job " + jobExecution.getJobInstance().getJobName() + " finished with status: " + jobExecution.getStatus());

	}
}
