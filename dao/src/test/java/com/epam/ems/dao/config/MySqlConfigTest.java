package com.epam.ems.dao.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

public class MySqlConfigTest {

    @Test
    void testDataSource(){
        Assertions.assertDoesNotThrow(()->{
            DataSource source = new MySqlDbConfig().mysqlDataSource();

            Assertions.assertNotNull(source);
        });
    }

}
