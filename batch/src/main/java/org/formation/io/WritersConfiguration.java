package org.formation.io;

import jakarta.annotation.Resource;
import org.formation.model.InputProduct;
import org.formation.model.OutputProduct;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.sql.DataSource;

@Configuration
public class WritersConfiguration {

    @Resource
    DataSource outputProductDataSource;

    @Bean
    @StepScope
    public FlatFileItemWriter<OutputProduct> flatFileproductWriter(@Value("#{stepExecutionContext['output.file.name']}") String filePath,
                                                                   @Value("#{stepExecutionContext['output.append']}") boolean shouldAppend) {
        return new FlatFileItemWriterBuilder<OutputProduct>()
                        .name("outputProductWriter")
                        .resource(new FileSystemResource(filePath))
                        .append(shouldAppend)
                        .delimited()
                        .names(new String[] { "reference", "nom", "hauteur", "largeur", "longueur"})
                        .headerCallback(writer -> writer.write("reference,nom,hauteur,largeur,longueur"))
                        .build();
    }

    @Bean
    @StepScope
    public StaxEventItemWriter<OutputProduct> xmlProductWriter(@Value("#{stepExecutionContext['output.file.name']}") String filePath) {
        return new StaxEventItemWriterBuilder<OutputProduct>().name("productXmlWriter").marshaller(productMarshaller())
                .resource(new FileSystemResource(filePath))
                .rootTagName("products-fournisseur1").overwriteOutput(true).build();
    }

    public Marshaller productMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(OutputProduct.class);
        return marshaller;
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<InputProduct> jdbcProductWriter() {
        JdbcBatchItemWriter<InputProduct> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(outputProductDataSource);
        itemWriter.setSql("INSERT INTO NEW_PRODUIT (nom,reference) VALUES (:nom, :reference)");

        itemWriter.setItemSqlParameterSourceProvider
                (new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }
}
