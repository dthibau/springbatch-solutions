package org.formation;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;


public class AppliStepListener {

	
	@BeforeStep
	public void retrieveInterstepData(StepExecution stepExecution) {
	    ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
	    ExecutionContext stepContext = stepExecution.getExecutionContext();
	    if ( stepExecution.getStepName().equals(Main.CSV2CSV_STEP) ) {
	    	stepContext.put("input.file.name", "file://" + jobContext.get("input.directory") + "/products.csv");
	    	stepContext.put("linesToSkip", 1);
	    	stepContext.put("tokenNames", new String[] { "id", "availability", "description", "hauteur", "largeur", "longueur", "nom", "prixUnitaire","reference","fournisseurId" });
	    	stepContext.put("skip.file.name", "file://" +jobContext.get("temp.directory") + "/skip.csv");
	    	stepContext.put("output.file.name", "file://" + jobContext.get("temp.directory") + "/products-out.csv");
	    } else if ( stepExecution.getStepName().equals(Main.BD2CSV_STEP) ) {
	    	stepContext.put("input.file.name", "file://" +jobContext.get("input.directory") + "/products.json");
	    	stepContext.put("skip.file.name", jobContext.get("temp.directory") + "/skip.json");
	    	stepContext.put("output.file.name", "file://" + jobContext.get("temp.directory") + "/products-out.csv");
	    } else {
	    	stepContext.put("input.file.name", "file://" + jobContext.get("temp.directory") + "/products-out.csv");
	    	stepContext.put("linesToSkip", 0);
	    	stepContext.put("tokenNames", new String[] { "reference", "nom", "hauteur", "largeur", "longueur" });
	    	stepContext.put("output.file.name", "file://" + jobContext.get("output.directory") + "/products.xml" );	    	
	    }
	    
	    System.out.println("Step Context "+ stepContext);

	  }
	

	@AfterRead
	public void afterRead(Object item) {
		System.out.println(item);
	}
}
