package org.formation;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
//@EnableBatchProcessing
public class Tp23Application {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(Tp23Application.class, args);

		for ( String s : context.getBeanDefinitionNames() ) {
			System.out.println( s );
		}
	}

}
