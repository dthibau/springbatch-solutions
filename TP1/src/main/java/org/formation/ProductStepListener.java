package org.formation;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
@JobScope
public class ProductStepListener {

    int nbSkipped = 0;

    @BeforeStep
    public void retrieveInterstepData(StepExecution stepExecution) {
        ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
        ExecutionContext stepContext = stepExecution.getExecutionContext();
        if (stepExecution.getStepName().equals(JobConfiguration.CSV2CSV_STEP)) {
            stepContext.put("input.file.name", jobContext.get("input.directory") + "/products.csv");
            stepContext.put("linesToSkip", 1);
            stepContext.put("tokenNames", new String[]{"id", "availability", "description", "hauteur", "largeur", "longueur", "nom", "prixUnitaire", "reference", "fournisseurId"});
            stepContext.put("skip.file.name", jobContext.get("temp.directory") + "/skip.csv");
            stepContext.put("output.file.name", jobContext.get("temp.directory") + "/products-out.csv");
            stepContext.put("output.append",false);
        } else if (stepExecution.getStepName().equals(JobConfiguration.JSON2CSV_STEP)) {
            stepContext.put("input.file.name", jobContext.get("input.directory") + "/products.json");
            stepContext.put("skip.file.name", jobContext.get("temp.directory") + "/skip.json");
            stepContext.put("output.file.name", jobContext.get("temp.directory") + "/products-out.csv");
            stepContext.put("output.append",true);
        } else {
            stepContext.put("input.file.name", jobContext.get("temp.directory") + "/products-out.csv");
            stepContext.put("linesToSkip", 0);
            stepContext.put("tokenNames", new String[]{"reference", "nom", "hauteur", "largeur", "longueur"});
            stepContext.put("output.file.name", jobContext.get("output.directory") + "/products.xml");
        }

        System.out.println("Step Context " + stepContext);

    }


    @OnSkipInProcess
    public void onSkipInProcess(Object item, Throwable t) {
        nbSkipped++;
        System.out.println("onSkipInProcess " + item + ":" + t.toString());
        System.out.println("NB SKIPPED " + nbSkipped);
    }
}
