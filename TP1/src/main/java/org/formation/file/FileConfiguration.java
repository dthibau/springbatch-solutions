package org.formation.file;

import org.formation.model.InputProduct;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class FileConfiguration {
	

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
	public FlatFileItemWriter<InputProduct> productWriter() {
		return new FlatFileItemWriterBuilder<InputProduct>()
			    .name("outputProductWriter")
			    .resource(new FileSystemResource("/home/dthibau/Formations/SpringBatch/MyWork/products-out.csv"))
			    .delimited()
			    .fieldExtractor(outputExtractor())
			    .build();

	}
	@Bean
	public FieldExtractor<InputProduct> outputExtractor() {
		BeanWrapperFieldExtractor<InputProduct> ret = new BeanWrapperFieldExtractor<>();
	  
	  ret.setNames(new String[] { "reference", "nom", "hauteur", "largeur", "longueur"});

	  return ret;
	}


}
