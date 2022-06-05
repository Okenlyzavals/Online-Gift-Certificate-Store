package com.epam.ems.service.validation.custom;


import com.epam.ems.dao.entity.criteria.CertificateCriteria;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CriteriaValidatorTest {

    private final CriteriaValidator validator = new CriteriaValidator();

    @Test
    void validateCorrectCriteria(){
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        Map<String, Object> valid = new LinkedHashMap<>();
        valid.put(CertificateCriteria.ParamName.TAG_NAMES.name(), List.of("tag","tog"));
        valid.put(CertificateCriteria.ParamName.ORDER_DATE_ASC.name(), "");
        valid.put(CertificateCriteria.ParamName.NAME_CONTAINS.name(), "anything");

        assertTrue(validator.isValid(valid, context));
    }

    @Test
    void validateIncorrectCriteria(){
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        Map<String, Object> valid = new LinkedHashMap<>();
        valid.put("I AM ERROR", "yeah");
        valid.put(CertificateCriteria.ParamName.ORDER_DATE_ASC.name(), "");
        valid.put(CertificateCriteria.ParamName.NAME_CONTAINS.name(), "anything");

        assertFalse(validator.isValid(valid, context));
    }

    @Test
    void validateNullCriteria(){
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

        assertFalse(validator.isValid(null, context));
    }
}
