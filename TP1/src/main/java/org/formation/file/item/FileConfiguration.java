package org.formation.file.item;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.formation.model.InputProduct;
import org.formation.model.OutputProduct;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
public class FileConfiguration {
	
	@Bean
	@StepScope
	public MultiResourceItemReader multiResourceReader(@Value("#{jobExecutionContext['temp.directory']}") String filePath) throws IOException {
		
		List<Resource> resourcesList = new ArrayList<>();
		Path dir = FileSystems.getDefault().getPath( filePath );
		DirectoryStream<Path> stream = Files.newDirectoryStream( dir, "*.csv" );
		for (Path path : stream) {
		    resourcesList.add(new FileSystemResource(path.toString())); 
		}
		Resource[] resources = new FileSystemResource[resourcesList.size()];
		for ( int i=0; i<resourcesList.size();i++) {
			resources[i] = resourcesList.get(i);
		}

		
	return new MultiResourceItemReaderBuilder<InputProduct>()
			.name("FinalCSVsReader")
	.delegate(productReader(null,null,-1))
	.resources(resources)
	.build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<InputProduct> productReader(@Value("#{stepExecutionContext['input.file.name']}") String filePath, 
			@Value("#{stepExecutionContext['tokenNames']}") String[] tokenNames, @Value("#{stepExecutionContext['linesToSkip']}") int linesToSkip) {
		
		FlatFileItemReader<InputProduct> reader = new FlatFileItemReader<InputProduct>();	
		reader.setResource(new FileSystemResource(filePath));
	    reader.setLinesToSkip(linesToSkip);   
	    //Configure how each line will be parsed and mapped to different values
	    reader.setLineMapper(new DefaultLineMapper<InputProduct>() {
	        {
	            //3 columns in each row
	            setLineTokenizer(new DelimitedLineTokenizer() {
	                {
	                    //setNames(new String[] { "id", "availability", "description", "hauteur", "largeur", "longueur", "nom", "prixUnitaire","reference","fournisseurId" });
	                	setNames(tokenNames);
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
	@StepScope
	public FlatFileItemWriter<OutputProduct> productWriter(@Value("#{stepExecutionContext['output.file.name']}") String filePath) {
		return new FlatFileItemWriterBuilder<OutputProduct>()
			    .name("outputProductWriter")
			    .resource(new FileSystemResource(filePath)).append(true)
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




}
