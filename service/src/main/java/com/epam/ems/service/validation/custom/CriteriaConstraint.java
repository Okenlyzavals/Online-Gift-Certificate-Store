package com.epam.ems.service.validation.custom;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CriteriaValidator.class)
@Target( { ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CriteriaConstraint {
    String message() default "msg.criteria.invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
