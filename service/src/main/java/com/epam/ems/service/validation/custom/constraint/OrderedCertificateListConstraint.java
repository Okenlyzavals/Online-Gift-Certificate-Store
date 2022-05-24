package com.epam.ems.service.validation.custom.constraint;

import com.epam.ems.service.validation.custom.OrderedCertificateListValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OrderedCertificateListValidator.class)
@Target( { ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderedCertificateListConstraint {
    String message() default "msg.order.certlist.invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
