package org.formation.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessorConfiguration {
	@Autowired
	ProductProcessor productProcessor;
	
	@Bean
	CompositeItemProcessor<InputProduct, OutputProduct> productProcessors() throws Exception {
		CompositeItemProcessor<InputProduct, OutputProduct> compositeProcessor = new CompositeItemProcessor<InputProduct, OutputProduct>();
		List itemProcessors = new ArrayList();
		itemProcessors.add(productValidator());
		itemProcessors.add(productProcessor);
		compositeProcessor.setDelegates(itemProcessors);

		return compositeProcessor;
	}

	@Bean
	public BeanValidatingItemProcessor<InputProduct> productValidator() throws Exception {
		BeanValidatingItemProcessor<InputProduct> validator = new BeanValidatingItemProcessor<>();
		validator.setFilter(false);
		validator.afterPropertiesSet();

		return validator;
	}
}
