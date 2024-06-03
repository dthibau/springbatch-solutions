package org.formation.file;

import org.formation.model.InputProduct;
import org.formation.model.OutputProduct;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FileConfiguration {

	@Value("${application.output-file}")
	String outputFile;
	@Autowired
	ProductProcessor productProcessor;

	@Bean
	public FlatFileItemReader<InputProduct> productReader() {
		
		FlatFileItemReader<InputProduct> reader = new FlatFileItemReader<InputProduct>();	
		reader.setResource(new ClassPathResource("/products.csv"));
	    reader.setLinesToSkip(1);   
	    //Configure how each line will be parsed and mapped to different values
	    reader.setLineMapper(new DefaultLineMapper<InputProduct>() {
	        {
	            //3 columns in each row
	            setLineTokenizer(new DelimitedLineTokenizer() {
	                {
	                    setNames(new String[] { "id", "availability", "description", "hauteur", "largeur", "longueur", "nom", "prixUnitaire","reference","fournisseurId" });
	                }
	            });
	            //Set values in Employee class
	            setFieldSetMapper(new BeanWrapperFieldSetMapper<InputProduct>() {
	                {
	                    setTargetType(InputProduct.class);
	                }
	            });
	        }
	    });
	    return reader;

	}
	
	@Bean
	public FlatFileItemWriter<OutputProduct> productWriter() {
		return new FlatFileItemWriterBuilder<OutputProduct>()
			    .name("outputProductWriter")
			    .resource(new FileSystemResource(outputFile))
			    .delimited()
			    .fieldExtractor(outputExtractor())
			    .build();

	}
	@Bean
	public FieldExtractor<OutputProduct> outputExtractor() {
		BeanWrapperFieldExtractor<OutputProduct> ret = new BeanWrapperFieldExtractor<>();
	  
	  ret.setNames(new String[] { "reference", "nom", "hauteur", "largeur", "longueur"});

	  return ret;
	}

	@Bean
	CompositeItemProcessor<InputProduct, OutputProduct> productProcessors() throws Exception {
		CompositeItemProcessor<InputProduct,OutputProduct> compositeProcessor =
				new CompositeItemProcessor<InputProduct,OutputProduct>();
		List itemProcessors = new ArrayList();
		itemProcessors.add(productProcessor);
		itemProcessors.add(productValidator());
		compositeProcessor.setDelegates(itemProcessors);

		return compositeProcessor;
	}

	@Bean
	public BeanValidatingItemProcessor<InputProduct> productValidator() throws Exception {
		BeanValidatingItemProcessor<InputProduct> validator = new BeanValidatingItemProcessor<>();
		validator.setFilter(true);
	//	validator.afterPropertiesSet();

		return validator;
	}


}
