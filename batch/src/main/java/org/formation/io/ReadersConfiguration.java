package org.formation.io;

import org.formation.model.InputProduct;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ReadersConfiguration {

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
}
