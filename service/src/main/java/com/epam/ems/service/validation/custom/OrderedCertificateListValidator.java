package com.epam.ems.service.validation.custom;

import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.validation.custom.constraint.OrderedCertificateListConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class OrderedCertificateListValidator
        implements ConstraintValidator<OrderedCertificateListConstraint, List<GiftCertificateDto>> {
    @Override
    public boolean isValid(List<GiftCertificateDto> value, ConstraintValidatorContext context) {
        return value != null && !value.isEmpty()
                && value.stream()
                .dropWhile(e -> e != null && e.getId() != null && e.getId()>0)
                .findAny()
                .isEmpty();
    }
}
