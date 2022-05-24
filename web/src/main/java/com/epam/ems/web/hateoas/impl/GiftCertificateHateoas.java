package com.epam.ems.web.hateoas.impl;

import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.web.controller.GiftCertificateController;
import com.epam.ems.web.hateoas.Hateoas;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

import static com.epam.ems.web.hateoas.constant.HateoasConstant.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateHateoas implements Hateoas<GiftCertificateDto> {
    @Override
    public GiftCertificateDto buildHateoas(GiftCertificateDto model) {
        model.add(linkTo(methodOn(GiftCertificateController.class)
                .getCertificate(model.getId()))
                .withSelfRel());
        model.add(linkTo(methodOn(GiftCertificateController.class)
                .getAllCerts(1,10))
                .withRel(ALL_CERTS));

        Map<String, Object> criteria = Map.of(
                CRITERIA_TAG_NAMES,
                new ArrayList<>(model.getTags()));
        model.add(linkTo(methodOn(GiftCertificateController.class)
                .getCertificatesByCriteria(criteria,1,10))
                .withRel(SAME_TAG_CERTS));

        model.add(linkTo(methodOn(GiftCertificateController.class)
                .delete(model.getId()))
                .withRel(DELETE));
        model.add(linkTo(methodOn(GiftCertificateController.class)
                .insert(model))
                .withRel(DUPLICATE));

        return model;
    }
}
