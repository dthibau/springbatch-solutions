package org.formation;

import java.util.Arrays;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableBatchProcessing
public class Tp1Application {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Tp1Application.class, args);
		
//		Arrays.asList(context.getBeanDefinitionNames()).stream()
//			 .filter(n -> n.toLowerCase().contains("job") || n.toLowerCase().contains("step") || n.toLowerCase().contains("batch") )
//		     .forEach(System.out::println);

	}


}
