package org.formation.io;

import jakarta.annotation.Resource;
import org.formation.model.InputProduct;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ReadersConfiguration {


    @Resource
    DataSource inputProductDataSource;

    @Bean
    @StepScope
    public FlatFileItemReader<InputProduct> productReader() {

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
        reader.setResource(new ClassPathResource("/products.csv"));
        reader.setLinesToSkip(1);
        //Configure how each line will be parsed and mapped to different values
        reader.setLineMapper(new DefaultLineMapper<InputProduct>() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[]{"id", "availability", "description", "hauteur", "largeur", "longueur", "nom", "prixUnitaire", "reference", "fournisseurId"});
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
    public JsonItemReader<InputProduct> jsonProductReader() {
        return new JsonItemReaderBuilder<InputProduct>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(InputProduct.class))
                .resource(new ClassPathResource("/products.json")).name("JsonProductReader").build();
    }


    @Bean
    @StepScope
    public JdbcPagingItemReader<InputProduct> jdbcProductReader() throws Exception {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("fournisseurId", 1);
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
