package com.epam.ems.service.validation.custom;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TagDtoListValidator.class)
@Target( { ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TagDtoListConstraint {
    String message() default "msg.taglist.invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
