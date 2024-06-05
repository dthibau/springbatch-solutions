package org.formation.bd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class DatasourceConfiguration {

    @Autowired
    private Environment env;
 
    @Primary
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public DataSource batchDataSource() {
 
        DriverManagerDataSource dataSource
          = new DriverManagerDataSource();

        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));

        return dataSource;
    }
    
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public DataSource inputProductDataSource() {
 
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(env.getProperty("application.input"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));

        return dataSource;
    }

    @Bean
    public DataSource outputProductDataSource() {
 
        DriverManagerDataSource dataSource
          = new DriverManagerDataSource();

        dataSource.setUrl(env.getProperty("application.output"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));

        return dataSource;
    }

}
