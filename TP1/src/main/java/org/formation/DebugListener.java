package org.formation;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.stereotype.Component;

@Component
public class DebugListener {

    @AfterRead
    public void afterRead(Object item) {
        System.out.println(item);
    }

    @AfterStep
    public void afterStep(StepExecution stepExecution) {
        System.out.println(stepExecution);
    }
}
