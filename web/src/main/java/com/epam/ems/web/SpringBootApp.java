package com.epam.ems.web;

import com.epam.ems.service.DatasourceFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class },
    scanBasePackages = "com.epam.ems")
@EnableSpringDataWebSupport
@Component
public class SpringBootApp {

    private DatasourceFiller filler;
    private Environment environment;

    @Autowired
    public SpringBootApp(DatasourceFiller filler, Environment environment) {
        this.filler = filler;
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class,args);
    }

    @PostConstruct
    public void fillDataSource(){
        if (Boolean.TRUE.equals(Boolean.valueOf(environment.getProperty("certificates.fillDB")))){
            filler.fillSourceWithData();
        }
    }

}
