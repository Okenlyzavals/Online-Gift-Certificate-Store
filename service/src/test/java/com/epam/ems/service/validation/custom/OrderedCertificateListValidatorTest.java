package com.epam.ems.service.validation.custom;

import com.epam.ems.service.dto.GiftCertificateDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderedCertificateListValidatorTest {

    private static final OrderedCertificateListValidator validator = new OrderedCertificateListValidator();

    @Test
    void validOrderedCertListTest(){
        GiftCertificateDto first = new GiftCertificateDto();
        first.setId(1L);
        GiftCertificateDto second = new GiftCertificateDto();
        second.setId(2L);
        GiftCertificateDto third = new GiftCertificateDto();
        third.setId(3L);

        List<GiftCertificateDto> valid = List.of(first,second,third);

        assertTrue(validator.isValid(valid, null));
    }

    @Test
    void invalidOrderedCertListTest(){
        GiftCertificateDto first = new GiftCertificateDto();
        first.setId(null);
        GiftCertificateDto second = new GiftCertificateDto();
        second.setId(-2L);

        List<GiftCertificateDto> invalid = List.of(first,second);

        assertFalse(validator.isValid(invalid, null));
    }

    @Test
    void nullOrderedCertListTest(){
        assertFalse(validator.isValid(null,null));
    }
}
