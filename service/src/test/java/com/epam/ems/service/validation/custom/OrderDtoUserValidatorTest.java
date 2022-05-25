package com.epam.ems.service.validation.custom;

import com.epam.ems.service.dto.UserDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderDtoUserValidatorTest {

    private static final OrderDtoUserValidator validator = new OrderDtoUserValidator();

    @Test
    void validUserTest(){
        UserDto valid = new UserDto(1L,"john","john@son.us","johnson1");
        assertTrue(validator.isValid(valid, null));
    }

    @Test
    void invalidUserTest(){
        UserDto invalid = new UserDto(-1L,"john","john@son.us","johnson1");
        assertFalse(validator.isValid(invalid, null));
    }

    @Test
    void nullUserTest(){
        assertFalse(validator.isValid(null, null));
    }

}
