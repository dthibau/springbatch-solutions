package org.formation.io;


import org.formation.model.InputProduct;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ReadersConfiguration {


    @jakarta.annotation.Resource
    DataSource inputProductDataSource;

    @Bean
    @StepScope
    public MultiResourceItemReader multiResourceReader(@Value("#{stepExecutionContext['temp.directory']}") String inputDirectory) throws IOException {

        Resource[] resources = new PathMatchingResourcePatternResolver()
                                .getResources("file://" + inputDirectory + "/products-*.csv");

        FlatFileItemReader<InputProduct> delegate =
                                new FlatFileItemReaderBuilder<InputProduct>()
                                        .name("delegateProductReader")
                                        .resource(new FileSystemResource( "/products.csv"))
                                        .linesToSkip(1)
                                        .delimited()
                                        .names("reference", "nom", "hauteur", "largeur", "longueur")
                                        .targetType(InputProduct.class)
                                        .build();

        return new MultiResourceItemReaderBuilder<InputProduct>()
                .name("FinalCSVsReader")
                .delegate(delegate)
                .resources(resources)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<InputProduct> productReader(@Value("#{stepExecutionContext['input.file.name']}") String filePath,
                                                          @Value("#{stepExecutionContext['tokenNames']}") String[] tokenNames,
                                                          @Value("#{stepExecutionContext['linesToSkip']}") int linesToSkip) {

       return new FlatFileItemReaderBuilder<InputProduct>()
                .name("flatFileproductReader")
                .resource(new ClassPathResource("/products.csv"))
                .linesToSkip(1)
                .delimited()
                .names("id", "availability", "description", "hauteur", "largeur", "longueur", "nom", "prixUnitaire", "reference", "fournisseurId")
                .targetType(InputProduct.class)
                .build();
   /*
        FlatFileItemReader<InputProduct> reader = new FlatFileItemReader<InputProduct>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(linesToSkip);
        //Configure how each line will be parsed and mapped to different values
        reader.setLineMapper(new DefaultLineMapper<InputProduct>() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(tokenNames);
                    }
                });
                //Set values in Employee class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<InputProduct>() {
                    {
                        setTargetType(InputProduct.class);
                    }
                });
            }
        });
        return reader;*/

    }

    @Bean
    @StepScope
    public JsonItemReader<InputProduct> jsonProductReader(@Value("#{stepExecutionContext['input.file.name']}") String filePath) {
        return new JsonItemReaderBuilder<InputProduct>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(InputProduct.class))
                .resource(new FileSystemResource(filePath)).name("JsonProductReader").build();
    }


    @Bean
    @StepScope
    public JdbcPagingItemReader<InputProduct> jdbcProductReader(@Value("#{stepExecutionContext['fournisseurId']}") Integer fournisseurId) throws Exception {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("fournisseurId", fournisseurId);
        return new JdbcPagingItemReaderBuilder<InputProduct>().name("productReader").dataSource(inputProductDataSource)
                .queryProvider(queryProvider()).parameterValues(parameterValues)
                .rowMapper(BeanPropertyRowMapper.newInstance(InputProduct.class)).pageSize(20).build();
    }

    @Bean
    @StepScope
    public PagingQueryProvider queryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
        provider.setDataSource(inputProductDataSource);
        provider.setSelectClause("select *");
        provider.setFromClause("from produit");
        provider.setWhereClause("where fournisseur_id=:fournisseurId");
        provider.setSortKey("id");
        return provider.getObject();
    }
}
