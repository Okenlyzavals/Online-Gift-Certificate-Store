package com.epam.ems.dao.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class MySqlDbConfig {

    private static final String DATABASE_DRIVER_CLASS_NAME = "spring.datasource.driver";
    private static final String DATABASE_URL = "spring.datasource.url";
    private static final String DATABASE_USERNAME = "spring.datasource.username";
    private static final String DATABASE_PASSWORD = "spring.datasource.password";

    private static final String DATABASE_HIBERNATE_DIALECT = "spring.jpa.database-platform";
    private static final String DATABASE_SHOW_SQL = "spring.jpa.show-sql";
    private static final String DATABASE_DDL_AUTO = "spring.jpa.hibernate.ddl-auto";
    private static final String SESSION_CONTEXT_CLASS="spring.jpa.properties.hibernate.current_session_context_class";

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment){
        this.environment = environment;
    }

    @Bean
    @Profile("prod")
    public DataSource mysqlDataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(environment.getProperty(DATABASE_DRIVER_CLASS_NAME));
        dataSource.setUrl(environment.getProperty(DATABASE_URL));
        dataSource.setUsername(environment.getProperty(DATABASE_USERNAME));
        dataSource.setPassword(environment.getProperty(DATABASE_PASSWORD));

        return dataSource;
    }

    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) throws IOException {
        Properties properties = new Properties();

        properties.put("hibernate.dialect", environment.getProperty(DATABASE_HIBERNATE_DIALECT));
        properties.put(DATABASE_SHOW_SQL, environment.getProperty(DATABASE_SHOW_SQL));
        properties.put("hibernate.hbm2ddl.auto", environment.getProperty(DATABASE_DDL_AUTO));
        properties.put(SESSION_CONTEXT_CLASS, environment.getProperty(SESSION_CONTEXT_CLASS));

        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setPackagesToScan("");
        factoryBean.setDataSource(dataSource);
        factoryBean.setHibernateProperties(properties);
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }

    @Bean
    @Profile("dev")
    public DataSource h2DataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("structure.sql")
                .addScript("data.sql")
                .build();
    }

    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
}
