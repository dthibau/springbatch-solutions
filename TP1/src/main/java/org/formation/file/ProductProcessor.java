package org.formation.file;

import org.formation.model.InputProduct;
import org.formation.model.OutputProduct;
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
	
	@BeforeProcess
	public void beforeProcess(InputProduct item) {
//        System.out.println("ItemProcessListener - beforeProcess");
    }
 
    @AfterProcess
    public void afterProcess(InputProduct item, OutputProduct result) {
//        System.out.println("ItemProcessListener - afterProcess");
    }
 
    @OnProcessError
    public void onProcessError(InputProduct item, Exception e) {
    	System.out.println("Erreur on "+item + " " + e.getMessage()); 	
    }
}
