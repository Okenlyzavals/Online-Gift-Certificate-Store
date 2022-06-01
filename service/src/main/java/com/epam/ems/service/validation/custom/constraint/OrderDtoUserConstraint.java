package com.epam.ems.service.validation.custom.constraint;

import com.epam.ems.service.validation.custom.OrderDtoUserValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OrderDtoUserValidator.class)
@Target( { ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderDtoUserConstraint {
    String message() default "msg.order.user.invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
