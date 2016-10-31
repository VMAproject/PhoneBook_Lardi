package com.phonebook.configuration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    private static final Logger LOGGER = LogManager.getLogger(DataSourceConfig.class);


    @Bean
    @Profile("mysql")
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(System.getProperty("jdbc:mysql://localhost:3306/phonebook"));
        dataSource.setUsername(System.getProperty("root"));
        dataSource.setPassword(System.getProperty("root"));
        dataSource.setInitialSize(10);
        dataSource.setMaxTotal(70);
        dataSource.setMaxIdle(30);
        return dataSource;
    }

    @Bean
    @Profile("default")
    public DataSource emptyDataSource() {
        return new BasicDataSource();
    }
}
