package com.epam.ems.service.validation.custom;

import com.epam.ems.dao.entity.criteria.Criteria;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CriteriaValidator implements ConstraintValidator<CriteriaConstraint, Map<String,Object>>{

    private static final List<String> CRITERIA_PARAM_NAMES = Arrays.stream(Criteria.ParamName.values())
            .map(Enum::name).collect(Collectors.toList());

    @Override
    public boolean isValid(Map<String, Object> value, ConstraintValidatorContext context) {

        boolean res = true;

        for (String key : value.keySet()){
            if (!CRITERIA_PARAM_NAMES.contains(key)) {
                res = false;
                break;
            }
            if (!key.equals(Criteria.ParamName.TAG_NAMES.name())
                    && !(value.get(key) instanceof String)){
                res = false;
                break;
            } else if(key.equals(Criteria.ParamName.TAG_NAMES.name())
                    && !(value.get(key) instanceof List
                        && !((List<?>) value.get(key)).isEmpty()
                        && ((List<?>) value.get(key)).get(0) instanceof String)
            ){
                res = false;
                break;
            }

        }

        return res;
    }
}
