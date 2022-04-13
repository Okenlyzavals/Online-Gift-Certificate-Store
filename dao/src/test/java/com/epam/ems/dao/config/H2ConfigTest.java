package com.epam.ems.dao.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class H2ConfigTest {

    @Test
    void testGetDataSource(){
        Assertions.assertNotNull(new H2DbConfig().h2DataSource());
    }

}
