package org.formation;

import jakarta.annotation.Resource;
import org.formation.model.InputProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(args = "toto")
public class ProductJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private Job fileJob;

    @Autowired
    private Step initStep;

    @Autowired
    private Step cleanUpStep;

    @Resource
    private MultiResourceItemReader<InputProduct> multiResourceReader;


    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
        jobLauncherTestUtils.setJob(fileJob);
    }

    @Test
    void testJobFullExecution() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.getJobLauncher().run(
                fileJob,
                new JobParametersBuilder()
                        .addString("id", "toto")
                        .toJobParameters()
        );

        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getStepExecutions().size()).isGreaterThan(0);
    }

    @Test
    void testInitStep() throws Exception {

        JobExecution jobExecution = jobLauncherTestUtils.launchStep(JobConfiguration.INIT_STEP);
        StepExecution stepExecution = jobExecution.getStepExecutions()
                .stream()
                .filter(se -> se.getStepName().equals(JobConfiguration.INIT_STEP))
                .findFirst()
                .orElseThrow();
        assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @Test
    void testCsv2XmlReader() throws Exception {

        // when
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution( new JobParametersBuilder()
                .addString("id", "toto")
                .toJobParameters());
        URL resourceUrl = getClass().getClassLoader().getResource("test-files");
        Path absolutePath = Paths.get(resourceUrl.toURI());
        stepExecution.getExecutionContext().put("temp.directory", absolutePath.toAbsolutePath().toString());
        stepExecution.getExecutionContext().put("input.file.name", absolutePath.toAbsolutePath().toString() + "/products-*.csv");
        stepExecution.getExecutionContext().put("linesToSkip", 0);
        stepExecution.getExecutionContext().put("tokenNames", new String[]{"reference", "nom", "hauteur", "largeur", "longueur"});
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            multiResourceReader.open(stepExecution.getExecutionContext());
            InputProduct inputProduct = multiResourceReader.read();
            assertThat(inputProduct).isNotNull();
            assertThat(inputProduct.getReference()).isEqualTo("RTEST");
            assertThat(inputProduct.getNom()).isEqualTo("test");
            multiResourceReader.close();
            return null;
        });
    }


}
