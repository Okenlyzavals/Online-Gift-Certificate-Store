package com.epam.ems.service.validation.custom.constraint;


import com.epam.ems.service.validation.custom.TagDtoListValidator;

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
