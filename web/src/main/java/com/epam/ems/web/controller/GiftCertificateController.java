package com.epam.ems.web.controller;

import com.epam.ems.service.GiftCertificateService;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.validation.OnCreate;
import com.epam.ems.service.validation.OnUpdate;
import com.epam.ems.service.validation.custom.CriteriaConstraint;
import com.epam.ems.web.hateoas.Hateoas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * API class for basic operations with gift certificates.
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
@Validated
@RestController
@RequestMapping(value = "/certificates")
@ComponentScan({"com.epam.ems.service.impl", "com.epam.ems.web.exception"})
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final Hateoas<GiftCertificateDto> hateoas;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService,
                                     Hateoas<GiftCertificateDto> hateoas) {
        this.giftCertificateService = giftCertificateService;
        this.hateoas = hateoas;
    }

    /**
     * Returns all certificates stored in data source
     * @return List of {@link GiftCertificateDto} retrieved from data source
     */
    @GetMapping
    public List<GiftCertificateDto> getAllCerts(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "5") @Min(1)int elements){
        List<GiftCertificateDto> certs = giftCertificateService.getAll(page,elements);
        certs.forEach(hateoas::buildHateoas);
        return certs;
    }

    /**
     * Returns certificate stored in data source under specific ID
     * @param id ID of a certificate
     * @return Instance of {@link GiftCertificateDto} retrieved from data source
     * @throws NoSuchEntityException if none was found
     */
    @GetMapping("/{id}")
    public GiftCertificateDto getCertificate(
            @PathVariable @Min(value = 1, message = "msg.id.negative") long id) throws NoSuchEntityException {
        return hateoas.buildHateoas(giftCertificateService.getById(id));
    }

    /**
     * Returns all certificates stored in data source
     * which match given criteria (filter)
     *
     * @param criteria A map containing filter as key and value to filter by as value.
     * @return List of {@link GiftCertificateDto} retrieved from data source
     */
    @GetMapping("/by_criteria")
    public List<GiftCertificateDto> getCertificatesByCriteria(
            @RequestBody
            @NotNull(message = "msg.dto.null")
            @CriteriaConstraint Map<String,Object> criteria,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "5") @Min(1)int elements){
        criteria.forEach((k,v)-> System.out.println(v.getClass()));
        List<GiftCertificateDto> certs = giftCertificateService.getByCriteria(criteria,page,elements);
        certs.forEach(hateoas::buildHateoas);
        return certs;
    }

    /**
     * Creates new gift certificate in data source
     * @param toCreate {@link GiftCertificateDto} to create
     */
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto insert(
            @RequestBody
            @NotNull(message = "msg.dto.null")
            @Validated({OnCreate.class})
                    GiftCertificateDto toCreate){
        return hateoas.buildHateoas(giftCertificateService.insert(toCreate));
    }

    /**
     * Updates given certificate in data source.
     * @param id ID of entity to update
     * @param toUpdate Entity to update, wrapped in {@link GiftCertificateDto}
     * @throws NoSuchEntityException if there is no such entity in Data Source.
     */
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

    /**
     * Removes certificate from Data Source by its ID.
     * @param id ID of certificate to delete.
     * @throws NoSuchEntityException if such certificate does not exist in data source.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(value = 1, message = "msg.id.negative") long id) throws NoSuchEntityException{
        giftCertificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
