package com.epam.ems.dao.config;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class MySqlDbConfig {

    @Bean
    @Profile("prod")
    public DataSource mysqlDataSource() throws Exception {
        Properties props = new Properties();
        props.load(getClass().getClassLoader().getResourceAsStream("db.properties"));
        return BasicDataSourceFactory.createDataSource(props);
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception{
        return new JdbcTransactionManager(mysqlDataSource());
    }
}
