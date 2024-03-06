package org.formation;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestContext {

	
	@Test
	public void contextLoad() {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/simple-job-launcher-context.xml");
		
		for ( String beanName : context.getBeanDefinitionNames() ) {
			System.out.println("Bean :" + beanName);
		}
		
	}
	
	@Test
	public void setupMySQL() {
		
		System.setProperty("ENVIRONMENT", "mysql");
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/simple-job-launcher-context.xml");
		
		for ( String beanName : context.getBeanDefinitionNames() ) {
			System.out.println("Bean :" + beanName);
		}
		
	}
}
