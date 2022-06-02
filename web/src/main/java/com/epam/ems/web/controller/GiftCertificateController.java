package com.epam.ems.web.controller;

import com.epam.ems.service.GiftCertificateService;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.validation.OnCreate;
import com.epam.ems.service.validation.OnUpdate;
import com.epam.ems.service.validation.custom.constraint.CriteriaConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

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
    private final RepresentationModelAssembler<GiftCertificateDto, GiftCertificateDto> hateoas;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService,
                                     RepresentationModelAssembler<GiftCertificateDto, GiftCertificateDto> hateoas) {
        this.giftCertificateService = giftCertificateService;
        this.hateoas = hateoas;
    }

    @GetMapping
    public PagedModel<GiftCertificateDto> getAllCerts(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "5") @Min(1) int size,
            PagedResourcesAssembler<GiftCertificateDto> assembler){
        Page<GiftCertificateDto> certs = giftCertificateService.getAll(page, size);
        return assembler.toModel(certs,hateoas);
    }

    @GetMapping("/{id}")
    public GiftCertificateDto getCertificate(
            @PathVariable @Min(value = 1, message = "msg.id.negative") long id) throws NoSuchEntityException {
        return hateoas.toModel(giftCertificateService.getById(id));
    }

    @GetMapping("/criteria")
    public PagedModel<GiftCertificateDto> getCertificatesByCriteria(
            @RequestBody
            @NotNull(message = "msg.dto.null")
            @CriteriaConstraint Map<String,Object> criteria,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "5") @Min(1) int size,
            PagedResourcesAssembler<GiftCertificateDto> assembler){
        Page<GiftCertificateDto> certs = giftCertificateService.getByCriteria(criteria,page, size);
        return assembler.toModel(certs, hateoas);
        }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto insert(
            @RequestBody
            @NotNull(message = "msg.dto.null")
            @Validated({OnCreate.class})
                    GiftCertificateDto toCreate){
        return hateoas.toModel(giftCertificateService.insert(toCreate));
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
        return hateoas.toModel(giftCertificateService.update(toUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(value = 1, message = "msg.id.negative") long id) throws NoSuchEntityException{
        giftCertificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
