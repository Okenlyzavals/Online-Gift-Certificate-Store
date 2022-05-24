package com.epam.ems.web.controller;

import com.epam.ems.service.GiftCertificateService;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.validation.OnCreate;
import com.epam.ems.service.validation.OnUpdate;
import com.epam.ems.service.validation.custom.constraint.CriteriaConstraint;
import com.epam.ems.web.hateoas.Hateoas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * API class for basic operations with gift certificates.
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
@Validated
@RestController
@RequestMapping(value = "/certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final Hateoas<GiftCertificateDto> hateoas;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService,
                                     Hateoas<GiftCertificateDto> hateoas) {
        this.giftCertificateService = giftCertificateService;
        this.hateoas = hateoas;
    }

    @GetMapping
    public CollectionModel<GiftCertificateDto> getAllCerts(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "5") @Min(1)int elements){
        List<GiftCertificateDto> certs = giftCertificateService.getAll(page,elements);
        certs.forEach(hateoas::buildHateoas);
        return hateoas.buildPaginationModel(certs,
                ()->page < 2 ? null : methodOn(getClass()).getAllCerts(1,elements),
                ()->certs.size() < elements ? null : methodOn(getClass()).getAllCerts(page+1, elements),
                ()->page < 2 ? null : methodOn(getClass()).getAllCerts(page-1, elements));
    }

    @GetMapping("/{id}")
    public GiftCertificateDto getCertificate(
            @PathVariable @Min(value = 1, message = "msg.id.negative") long id) throws NoSuchEntityException {
        return hateoas.buildHateoas(giftCertificateService.getById(id));
    }

    @GetMapping("/criteria")
    public CollectionModel<GiftCertificateDto> getCertificatesByCriteria(
            @RequestBody
            @NotNull(message = "msg.dto.null")
            @CriteriaConstraint Map<String,Object> criteria,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "5") @Min(1)int elements){
        List<GiftCertificateDto> certs = giftCertificateService.getByCriteria(criteria,page,elements);
        certs.forEach(hateoas::buildHateoas);
        return hateoas.buildPaginationModel(certs,
                ()->page < 2 ? null : methodOn(getClass()).getCertificatesByCriteria(criteria,1,elements),
                ()->certs.size() < elements ? null : methodOn(getClass()).getCertificatesByCriteria(criteria,page+1, elements),
                ()->page < 2 ? null : methodOn(getClass()).getCertificatesByCriteria(criteria,page-1, elements));
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto insert(
            @RequestBody
            @NotNull(message = "msg.dto.null")
            @Validated({OnCreate.class})
                    GiftCertificateDto toCreate){
        return hateoas.buildHateoas(giftCertificateService.insert(toCreate));
    }

    @PatchMapping(value = "/{id}",
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto update(@PathVariable @Min(value = 1, message = "msg.id.negative") long id,
                                 @RequestBody
                                 @NotNull(message = "msg.dto.null")
                                 @Validated(OnUpdate.class) GiftCertificateDto toUpdate)
            throws NoSuchEntityException{
        toUpdate.setId(id);
        return hateoas.buildHateoas(giftCertificateService.update(toUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(value = 1, message = "msg.id.negative") long id) throws NoSuchEntityException{
        giftCertificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
