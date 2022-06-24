package com.epam.ems.web.hateoas;

import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.web.controller.GiftCertificateController;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


import static com.epam.ems.web.hateoas.constant.HateoasConstant.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateModelAssembler
        implements RepresentationModelAssembler<GiftCertificateDto,GiftCertificateDto> {
    @Override
    public GiftCertificateDto toModel(GiftCertificateDto entity) {

        return entity.add(
                linkTo(methodOn(GiftCertificateController.class).getCertificate(entity.getId())).withSelfRel(),
                linkTo(GiftCertificateController.class).withRel(ALL_CERTS),
                linkTo(methodOn(GiftCertificateController.class).delete(entity.getId())).withRel(DELETE),
                linkTo(methodOn(GiftCertificateController.class).insert(entity)).withRel(DUPLICATE));
    }
}
