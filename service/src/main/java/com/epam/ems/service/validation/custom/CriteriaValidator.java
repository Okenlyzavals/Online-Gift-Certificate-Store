package com.epam.ems.service.validation.custom;

import com.epam.ems.dao.entity.criteria.Criteria;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CriteriaValidator implements ConstraintValidator<CriteriaConstraint, Map<String,String>>{

    private static final List<String> CRITERIA_PARAM_NAMES = Arrays.stream(Criteria.ParamName.values()).map(Enum::name).collect(Collectors.toList());

    @Override
    public boolean isValid(Map<String, String> value, ConstraintValidatorContext context) {

        if(value == null){
            return false;
        }

        return CRITERIA_PARAM_NAMES.containsAll(value.keySet());
    }
}
