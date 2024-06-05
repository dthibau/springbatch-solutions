package org.formation.io;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class InitTasklet implements Tasklet {

    @Value("${application.source-directory}")
    private String sourceDirectory;
    @Value("${application.input-directory}")
    private String inputDirectory;
    @Value("${application.temp-directory}")
    private String tempDirectory;
    @Value("${application.output-directory}")
    private String outputDirectory;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        _checkDir(inputDirectory);
        _checkDir(tempDirectory);
        _checkDir(outputDirectory);
        _cleanDirectory(outputDirectory);

        ExecutionContext taskletContext = contribution.getStepExecution().getExecutionContext();
        taskletContext.put("input.directory", inputDirectory);
        taskletContext.put("temp.directory", tempDirectory);
        taskletContext.put("output.directory", outputDirectory);


        File sourceDir = new File(sourceDirectory);

        File[] files = sourceDir.listFiles();
        for (File file : files ) {
            Path copied = Paths.get(inputDirectory + "/" + file.getName());
            Files.copy(Paths.get(file.toURI()), copied, StandardCopyOption.REPLACE_EXISTING);
        }

        return RepeatStatus.FINISHED;
    }

    private void _checkDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Assert.isTrue(dir.isDirectory(), dir + " exists and is not a directory");

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

    }

}