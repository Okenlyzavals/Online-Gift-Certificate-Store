package com.epam.ems.service.validation.custom;

import com.epam.ems.service.dto.TagDto;
import com.epam.ems.service.validation.custom.constraint.TagDtoListConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class TagDtoListValidator implements ConstraintValidator<TagDtoListConstraint, Set<TagDto>> {

    @Override
    public boolean isValid(Set<TagDto> value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        AtomicBoolean result = new AtomicBoolean(true);
        value.forEach(e->{
            if(e.getName() == null || e.getName().length()>45 || e.getName().length()<2){
                result.set(false);
            }
        });
        return result.get();
    }
}
