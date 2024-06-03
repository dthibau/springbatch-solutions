package org.formation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class Tp1Application {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(Tp1Application.class, args);

		Arrays.asList(context.getBeanDefinitionNames()).stream()
				.filter(n -> n.toLowerCase().contains("job") || n.toLowerCase().contains("step") || n.toLowerCase().contains("batch") )
				.forEach(System.out::println);

	}

}
