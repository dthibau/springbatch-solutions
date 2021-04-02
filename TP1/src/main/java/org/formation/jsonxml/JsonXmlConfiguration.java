package org.formation.jsonxml;

import java.util.ArrayList;
import java.util.List;

import org.formation.model.InputProduct;
import org.formation.model.OutputProduct;
import org.formation.model.ProductProcessor;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class JsonXmlConfiguration {

	@Autowired
	ProductProcessor productProcessor;

	@Bean
	public JsonItemReader<InputProduct> jsonProductReader() {
		return new JsonItemReaderBuilder<InputProduct>()
				.jsonObjectReader(new JacksonJsonObjectReader<>(InputProduct.class))
				.resource(new ClassPathResource("products.json")).name("JsonProductReader").build();
	}

	
	
}
