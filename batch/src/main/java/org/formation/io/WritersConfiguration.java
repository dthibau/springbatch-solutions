package org.formation.io;

import org.formation.model.InputProduct;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class WritersConfiguration {

    @Value("${application.output-file}")
	String outputFile;

    @Bean
	public FlatFileItemWriter<InputProduct> productWriter() {
        return new FlatFileItemWriterBuilder<InputProduct>()
                        .name("outputProductWriter")
                        .resource(new FileSystemResource(outputFile))
                        .delimited()
                        .names(new String[] { "reference", "nom", "hauteur", "largeur", "longueur"})
                        .build();
    }
}
