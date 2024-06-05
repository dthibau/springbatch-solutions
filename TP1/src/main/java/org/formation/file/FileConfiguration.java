package org.formation.file;

import org.formation.model.InputProduct;
import org.formation.model.OutputProduct;
import org.springframework.batch.core.configuration.annotation.StepScope;
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
import org.springframework.beans.factory.annotation.Value;
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
	public FlatFileItemWriter<OutputProduct> productWriter(@Value("#{stepExecutionContext['output.file.name']}") String filePath,
														   @Value("#{stepExecutionContext['output.append']}") boolean shouldAppend) {
		return new FlatFileItemWriterBuilder<OutputProduct>()
				.name("outputProductWriter")
				.resource(new FileSystemResource(filePath))
				.append(shouldAppend)
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
		itemProcessors.add(productValidator());
		itemProcessors.add(productProcessor);
		compositeProcessor.setDelegates(itemProcessors);

		return compositeProcessor;
	}

	@Bean
	public BeanValidatingItemProcessor<InputProduct> productValidator() throws Exception {
		BeanValidatingItemProcessor<InputProduct> validator = new BeanValidatingItemProcessor<>();
		validator.setFilter(false);
	//	validator.afterPropertiesSet();

		return validator;
	}


}
