package org.formation.model;

import org.springframework.batch.core.annotation.AfterProcess;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProductProcessor implements ItemProcessor<InputProduct, OutputProduct> {
	
	@Override
	public OutputProduct process(InputProduct item) throws Exception {
		if ( item.getFournisseurId() == 1 ) {
    		return new OutputProduct(item);
    	} else {
    		return null;
    	}
	}
	

}
