package org.formation.jsonxml;

import org.formation.model.InputProduct;
import org.formation.model.OutputProduct;
import org.formation.model.ProductProcessor;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

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

	@Bean
	public StaxEventItemWriter<OutputProduct> productXmlWriter() {
		return new StaxEventItemWriterBuilder<OutputProduct>().name("productXmlWriter").marshaller(productMarshaller())
				.resource(new FileSystemResource("/home/dthibau/Formations/SpringBatch/MyWork/products-out.xml"))
				.rootTagName("products-fournisseur1").overwriteOutput(true).build();
	}

	public Marshaller productMarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

		marshaller.setClassesToBeBound(OutputProduct.class);

		return marshaller;

	}
}
