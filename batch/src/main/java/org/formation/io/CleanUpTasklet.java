package org.formation.io;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CleanUpTasklet implements Tasklet {

    @Value("${application.input-directory}")
    private String inputDirectory;
    @Value("${application.temp-directory}")
    private String tempDirectory;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        _cleanDirectory(inputDirectory);
        _cleanDirectory(tempDirectory);

        return RepeatStatus.FINISHED;
    }

    private void _cleanDirectory(String directory) {
        File dir = new File(directory);

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            boolean deleted = files[i].delete();
            if (!deleted) {
                throw new UnexpectedJobExecutionException("Could not delete file " + files[i].getPath());
            }
        }
        dir.delete();
    }

}
