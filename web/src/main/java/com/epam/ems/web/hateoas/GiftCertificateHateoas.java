package com.epam.ems.web.hateoas;

import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.web.controller.GiftCertificateController;
import com.epam.ems.web.controller.OrderController;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

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
                .withRel("allCertificates"));

        Map<String, Object> criteria = Map.of(
                "TAG_NAMES",
                new ArrayList<>(model.getTags()));
        model.add(linkTo(methodOn(GiftCertificateController.class)
                .getCertificatesByCriteria(criteria,1,10))
                .withRel("certsWithSameTags"));

        model.add(linkTo(methodOn(GiftCertificateController.class)
                .delete(model.getId()))
                .withRel("delete"));
        model.add(linkTo(methodOn(GiftCertificateController.class)
                .insert(model))
                .withRel("duplicate"));

        return model;
    }
}
