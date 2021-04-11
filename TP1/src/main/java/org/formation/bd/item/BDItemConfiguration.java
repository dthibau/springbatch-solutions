package org.formation.bd.item;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.formation.model.InputProduct;
import org.formation.model.OutputProduct;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Configuration
public class BDItemConfiguration {

	@Resource
	DataSource inputProductDataSource;
	
	@Resource
	DataSource outputProductDataSource;

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
		provider.setSelectClause("select id, hauteur,largeur,longueur,nom,reference");
		provider.setFromClause("from produit");
		provider.setWhereClause("where fournisseur_id=:fournisseurId");
		provider.setSortKey("id");
		return provider.getObject();
	}
	
	@Bean
    @StepScope
    public JdbcBatchItemWriter<InputProduct> jdbcProductWriter()
    {
        JdbcBatchItemWriter<InputProduct> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(outputProductDataSource);
        itemWriter.setSql("INSERT INTO NEW_PRODUIT (nom,reference) VALUES (:nom, :reference)");
 
        itemWriter.setItemSqlParameterSourceProvider
            (new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.afterPropertiesSet();
         
        return itemWriter;
    }
}
