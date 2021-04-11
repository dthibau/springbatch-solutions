package org.formation.file;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class AppliStepListener {

	
	@BeforeStep
	public void retrieveInterstepData(StepExecution stepExecution) {
	    ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
	    ExecutionContext stepContext = stepExecution.getExecutionContext();
	    if ( stepExecution.getStepName().equals(JobConfiguration.CSV2CSV_STEP) ) {
	    	stepContext.put("input.file.name", jobContext.get("input.directory") + "/products.csv");
	    	stepContext.put("linesToSkip", 1);
	    	stepContext.put("tokenNames", new String[] { "id", "availability", "description", "hauteur", "largeur", "longueur", "nom", "prixUnitaire","reference","fournisseurId" });
	    	stepContext.put("skip.file.name", jobContext.get("temp.directory") + "/skip.csv");
	    	stepContext.put("output.file.name", jobContext.get("temp.directory") + "/products-csv-out.csv");
	    } else if ( stepExecution.getStepName().equals(JobConfiguration.JSON2CSV_STEP) ) {
	    	stepContext.put("input.file.name", jobContext.get("input.directory") + "/products.json");
	    	stepContext.put("skip.file.name", jobContext.get("temp.directory") + "/skip.json");
	    	stepContext.put("output.file.name", jobContext.get("temp.directory") + "/products-json-out.csv");
	    } else {
	    	stepContext.put("input.file.name", jobContext.get("temp.directory") + "/products*.csv");
	    	stepContext.put("linesToSkip", 0);
	    	stepContext.put("tokenNames", new String[] { "reference", "nom", "hauteur", "largeur", "longueur" });
	    	stepContext.put("output.file.name", jobContext.get("output.directory") + "/products.xml" );	    	
	    }
	    
	    System.out.println("Step Context "+ stepContext);

	  }
	

	@AfterStep
	ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("Step Context "+ stepExecution);
		return stepExecution.getExitStatus();
	}
}
