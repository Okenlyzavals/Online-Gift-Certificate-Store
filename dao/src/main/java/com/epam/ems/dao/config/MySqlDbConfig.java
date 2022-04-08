package com.epam.ems.dao.config;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class MySqlDbConfig {

    @Bean
    @Qualifier(value = "mySql")
    public DataSource mysqlDataSource() throws Exception {
        Properties props = new Properties();
        props.load(getClass().getClassLoader().getResourceAsStream("db.properties"));
        return BasicDataSourceFactory.createDataSource(props);
    }
}
