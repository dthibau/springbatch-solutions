package org.formation.bd;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.formation.model.InputProduct;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Configuration
public class BDJobConfiguration {

	@Resource
	DataSource inputProductDataSource;

	@Bean
	public JdbcPagingItemReader<InputProduct> jdbcProductReader() throws Exception {
		Map<String, Object> parameterValues = new HashMap<>();
		parameterValues.put("fournisseurId", 1);
		return new JdbcPagingItemReaderBuilder<InputProduct>().name("productReader").dataSource(inputProductDataSource)
				.queryProvider(queryProvider()).parameterValues(parameterValues)
				.rowMapper(BeanPropertyRowMapper.newInstance(InputProduct.class)).pageSize(20).build();
	}

	@Bean
	public PagingQueryProvider queryProvider() throws Exception {
		SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
		provider.setDataSource(inputProductDataSource);
		provider.setSelectClause("select id, hauteur,largeur,longueur,nom,reference");
		provider.setFromClause("from produit");
		provider.setWhereClause("where fournisseur_id=:fournisseurId");
		provider.setSortKey("id");
		return provider.getObject();
	}
}
