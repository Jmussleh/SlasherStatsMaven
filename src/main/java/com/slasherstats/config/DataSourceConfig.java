package com.slasherstats.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
/**
 * Configuration class responsible for setting up the application's
 * connection to the MySQL database.
 */
@Configuration
public class DataSourceConfig {
    /**
     * Creates and configures a {@link DataSource} bean for MySQL database connectivity.
     *
     * @return a configured {@code DataSource} instance
     * @throws IllegalStateException if database configuration is incomplete or invalid
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(DbConfig.getJdbcUrl());
        ds.setUsername(DbConfig.getUsername());
        ds.setPassword(DbConfig.getPassword());
        return ds;
    }
}
