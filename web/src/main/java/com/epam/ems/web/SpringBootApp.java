package com.epam.ems.web;

import com.epam.ems.service.DatasourceFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = {"com.epam.ems"})
@EnableSpringDataWebSupport
@EnableJpaRepositories("com.epam.ems")
@EntityScan("com.epam.ems")
@Component
public class SpringBootApp {

    private final DatasourceFiller filler;
    private final Environment environment;
    @Autowired
    PasswordEncoder encoder;

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
