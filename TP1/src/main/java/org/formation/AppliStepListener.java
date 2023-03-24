package org.formation;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
@JobScope
public class AppliStepListener {

	String skipFile = "";
	
	
	@BeforeStep
	public void retrieveInterstepData(StepExecution stepExecution) {
	    ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
	    ExecutionContext stepContext = stepExecution.getExecutionContext();
	    if ( stepExecution.getStepName().equals(JobConfiguration.CSV2CSV_STEP) ) {
	    	stepContext.put("input.file.name", jobContext.get("input.directory") + "/products.csv");
	    	stepContext.put("linesToSkip", 1);
	    	stepContext.put("tokenNames", new String[] { "id", "availability", "description", "hauteur", "largeur", "longueur", "nom", "prixUnitaire","reference","fournisseurId" });
	    	skipFile = jobContext.get("temp.directory") + "/skip.csv";
	    	stepContext.put("output.file.name", jobContext.get("temp.directory") + "/products-out.csv");
	    } else if ( stepExecution.getStepName().equals(JobConfiguration.JSON2CSV_STEP) ) {
	    	stepContext.put("input.file.name", jobContext.get("input.directory") + "/products.json");
	    	skipFile = jobContext.get("temp.directory") + "/skip.json";
	    	stepContext.put("output.file.name", jobContext.get("temp.directory") + "/products-out.csv");
	    } else {
	    	stepContext.put("input.file.name", jobContext.get("temp.directory") + "/products-out.csv");
	    	stepContext.put("linesToSkip", 0);
	    	stepContext.put("tokenNames", new String[] { "reference", "nom", "hauteur", "largeur", "longueur" });
	    	stepContext.put("output.file.name", jobContext.get("output.directory") + "/products.xml" );	    	
	    }
	    
	    System.out.println("Step Context "+ stepContext);

	  }
	
	  @OnSkipInRead
	  void onSkipInRead(Throwable t) {
		System.out.println(skipFile + "/" + t); 
	  }
	  @OnSkipInProcess
	  void onSkipInProcess(Object item, Throwable t) {
		  System.out.println(skipFile + " ** " + item + " ** " + t.getMessage());
	  }
	  @OnSkipInWrite
	  void onSkipInWrite(Object item, Throwable t) {
		  System.out.println(skipFile + "/" + item + "/" + t);
	  }
	
	
	
	@AfterRead
	public void afterRead(Object item) {
//		System.out.println(item);
	}
}
