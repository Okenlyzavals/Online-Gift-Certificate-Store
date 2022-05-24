package com.epam.ems.service.validation.custom;

import com.epam.ems.dao.entity.criteria.Criteria;
import com.epam.ems.service.validation.custom.constraint.CriteriaConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CriteriaValidator implements ConstraintValidator<CriteriaConstraint, Map<String,Object>>{

    private static final List<String> CRITERIA_PARAM_NAMES =
            Arrays.stream(Criteria.ParamName.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());

    @Override
    public boolean isValid(Map<String, Object> value, ConstraintValidatorContext context) {
        return value != null && value.keySet()
                .stream()
                .dropWhile(e -> validateParamNames(e)
                        && validateTagNameSearch(e, value)
                        && validateNotTagNameSearch(e, value))
                .findAny()
                .isEmpty();
    }

    private boolean validateParamNames(String key){
        return CRITERIA_PARAM_NAMES.contains(key);
    }

    private boolean validateNotTagNameSearch(String key, Map<String, Object> value){
        return key.equals(Criteria.ParamName.TAG_NAMES.name())
                || value.get(key) instanceof String;
    }

    private boolean validateTagNameSearch(String key, Map<String, Object> value){
        return !key.equals(Criteria.ParamName.TAG_NAMES.name())
                || (value.get(key) instanceof List
                    && !((List<?>) value.get(key)).isEmpty()
                    && ((List<?>) value.get(key)).get(0) instanceof String);
    }


}
