package org.formation.io;

import org.formation.model.InputProduct;
import org.formation.model.OutputProduct;
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

@Configuration
public class WritersConfiguration {

    @Value("${application.output-file}")
	String outputFile;

    @Value("${application.output-xml}")
    private String outputXml;

    @Bean
	public FlatFileItemWriter<OutputProduct> productWriter() {
        return new FlatFileItemWriterBuilder<OutputProduct>()
                        .name("outputProductWriter")
                        .resource(new FileSystemResource(outputFile))
                        .delimited()
                        .names(new String[] { "reference", "nom", "hauteur", "largeur", "longueur"})
                        .headerCallback(writer -> writer.write("reference,nom,hauteur,largeur,longueur"))
                        .build();
    }

    @Bean
    public StaxEventItemWriter<OutputProduct> xmlProductWriter() {
        return new StaxEventItemWriterBuilder<OutputProduct>().name("productXmlWriter").marshaller(productMarshaller())
                .resource(new FileSystemResource(outputXml))
                .rootTagName("products-fournisseur1").overwriteOutput(true).build();
    }

    public Marshaller productMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(OutputProduct.class);
        return marshaller;
    }

}
