package org.formation.io;

import org.formation.model.InputProduct;
import org.formation.model.OutputProduct;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ProcessorsConfiguration {


    /**
     * This processor filters products based on the fournisseurId.
     * If fournisseurId is 1, it converts InputProduct to OutputProduct.
     * Otherwise, it returns null, effectively filtering out the item.
     *
     * @return ItemProcessor that processes InputProduct to OutputProduct
     */

    @Bean
    public ItemProcessor<InputProduct, OutputProduct> mapProcessor() {
        return item -> {
            // Example processing logic: convert to uppercase
            if (item.getFournisseurId() == 1 || item.getFournisseurId() == 0) {
                return new OutputProduct(item);
            } else {
                return null;
            }
        };
    }

    @Bean
    CompositeItemProcessor<InputProduct, OutputProduct> productProcessors() throws Exception {
        CompositeItemProcessor<InputProduct, OutputProduct> compositeProcessor =
                new CompositeItemProcessor<InputProduct, OutputProduct>();
        List itemProcessors = new ArrayList();
        itemProcessors.add(productValidator());
        itemProcessors.add(mapProcessor());
        compositeProcessor.setDelegates(itemProcessors);

        return compositeProcessor;
    }

    @Bean
    public BeanValidatingItemProcessor<InputProduct> productValidator() throws Exception {
        BeanValidatingItemProcessor<InputProduct> validator = new BeanValidatingItemProcessor<>();
        // validator.setFilter(true);
        return validator;
    }
}



