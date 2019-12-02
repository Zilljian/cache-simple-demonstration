package org.db.introduction.cache_simple_demonstration.configuration;

import org.db.introduction.cache_simple_demonstration.model.PostgresConnectionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class PostgresConfiguration {

    @Bean
    @ConfigurationProperties("postgres")
    PostgresConnectionProperties postgresConnectionProperties() {
        return new PostgresConnectionProperties();
    }

    @Bean
    public DataSource mysqlDataSource(PostgresConnectionProperties properties) {
        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.getDriver());
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getLogin());
        dataSource.setPassword(properties.getPassword());
        return dataSource;
    }
}
