package org.formation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
public class BDConfiguration {


    @Autowired
    private Environment env;
 
    @Primary
    @Bean
    public DataSource batchDataSource() {
 
        DriverManagerDataSource dataSource
          = new DriverManagerDataSource();

        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));

        return dataSource;
    }
    
    @Bean
    public DataSource inputProductDataSource() {
 
        DriverManagerDataSource dataSource
          = new DriverManagerDataSource();

        dataSource.setUrl(env.getProperty("application.input-bd"));
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
