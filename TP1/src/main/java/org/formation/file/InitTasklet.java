package org.formation.file;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class InitTasklet implements Tasklet {

	@Value("${appli.source.directory}")
	private String sourceDirectory;
	@Value("${appli.input.directory}")
	private String inputDirectory;
	@Value("${appli.temp.directory}")
	private String tempDirectory;
	@Value("${appli.output.directory}")
	private String outputDirectory;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		_checkDir(inputDirectory);
		_checkDir(tempDirectory);
		_checkDir(outputDirectory);

		ExecutionContext taskletContext = contribution.getStepExecution().getExecutionContext();
		taskletContext.put("input.directory", inputDirectory);
		taskletContext.put("temp.directory", tempDirectory);
		taskletContext.put("output.directory", outputDirectory);

		File sourceDir = new File(sourceDirectory);

		File[] files = sourceDir.listFiles();
		if (files.length == 0) {
			contribution.getStepExecution().setExitStatus(new ExitStatus(JobConfiguration.NOFILES));
		} else {
			for (File file : files) {
				Path copied = Paths.get(inputDirectory + "/" + file.getName());
				Files.copy(Paths.get(file.toURI()), copied, StandardCopyOption.REPLACE_EXISTING);
			}
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
}
