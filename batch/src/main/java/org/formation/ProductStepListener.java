package org.formation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
//@JobScope
public class ProductStepListener {

    int nbSkipped = 0;

    String skipFile;
    Path filePath = Paths.get("output.txt");

    @Autowired
    ObjectMapper objectMapper;

    @BeforeStep
    public void retrieveInterstepData(StepExecution stepExecution) {
        ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
        ExecutionContext stepContext = stepExecution.getExecutionContext();
        if (stepExecution.getStepName().equals(JobConfiguration.CSV2CSV_STEP)) {
            stepContext.put("input.file.name", jobContext.get("input.directory") + "/products.csv");
            stepContext.put("linesToSkip", 1);
            stepContext.put("tokenNames", new String[]{"id", "availability", "description", "hauteur", "largeur", "longueur", "nom", "prixUnitaire", "reference", "fournisseurId"});
            skipFile = jobContext.get("output.directory") + "/skip.csv";
            filePath = null;
            stepContext.put("output.file.name", jobContext.get("temp.directory") + "/products-csv-out.csv");
            stepContext.put("output.append",false);
        } else if (stepExecution.getStepName().equals(JobConfiguration.JSON2CSV_STEP)) {
            stepContext.put("input.file.name", jobContext.get("input.directory") + "/products.json");
            skipFile = jobContext.get("output.directory") + "/skip.json";
            filePath = null;
            stepContext.put("output.file.name", jobContext.get("temp.directory") + "/products-json-out.csv");
            stepContext.put("output.append",true);
        } else {
            stepContext.put("input.file.name", jobContext.get("temp.directory") + "/products-*.csv");
            stepContext.put("linesToSkip", 0);
            stepContext.put("tokenNames", new String[]{"reference", "nom", "hauteur", "largeur", "longueur"});
            stepContext.put("output.file.name", jobContext.get("output.directory") + "/products.xml");
        }

        System.out.println("Step Context " + stepContext);

    }


    @OnSkipInProcess
    public void onSkipInProcess(Object item, Throwable t) throws IOException {
        nbSkipped++;

        if ( filePath == null ) {
            filePath = Paths.get(skipFile);
        }
        Files.write(filePath, (objectMapper.writeValueAsString(item) + "\n").getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
    }
}
