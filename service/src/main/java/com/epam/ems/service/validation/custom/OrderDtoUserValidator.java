package com.epam.ems.service.validation.custom;

import com.epam.ems.service.dto.UserDto;
import com.epam.ems.service.validation.custom.constraint.OrderDtoUserConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OrderDtoUserValidator implements ConstraintValidator<OrderDtoUserConstraint, UserDto> {
    @Override
    public boolean isValid(UserDto value, ConstraintValidatorContext context) {
        return value!= null && value.getId() != null && value.getId()>0;
    }
}
