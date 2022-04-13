package com.epam.ems.service.validation.custom;


import com.epam.ems.dao.entity.criteria.Criteria;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class CriteriaValidatorTest {

    private final CriteriaValidator validator = new CriteriaValidator();

    @Test
    void validateCorrectCriteria(){
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        Map<String, String> valid = new LinkedHashMap<>();
        valid.put(Criteria.ParamName.TAG_NAME.name(), "tag");
        valid.put(Criteria.ParamName.ORDER_DATE_ASC.name(), "");
        valid.put(Criteria.ParamName.NAME_CONTAINS.name(), "anything");

        assertTrue(validator.isValid(valid, context));
    }

    @Test
    void validateIncorrectCriteria(){
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        Map<String, String> valid = new LinkedHashMap<>();
        valid.put("I AM ERROR", "yeah");
        valid.put(Criteria.ParamName.ORDER_DATE_ASC.name(), "");
        valid.put(Criteria.ParamName.NAME_CONTAINS.name(), "anything");

        assertFalse(validator.isValid(valid, context));
    }

    @Test
    void validateNullCriteria(){
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

        assertFalse(validator.isValid(null, context));
    }
}
