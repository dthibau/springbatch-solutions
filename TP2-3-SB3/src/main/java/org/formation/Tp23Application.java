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
			if ( s.toLowerCase().contains("job") || s.toLowerCase().contains("step") || s.toLowerCase().contains("batch") ) {
				System.out.println( s );
			}
		}
	}

}
