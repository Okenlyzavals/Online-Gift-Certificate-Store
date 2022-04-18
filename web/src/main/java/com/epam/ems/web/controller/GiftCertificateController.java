package com.epam.ems.web.controller;

import com.epam.ems.service.GiftCertificateService;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.validation.OnCreate;
import com.epam.ems.service.validation.OnUpdate;
import com.epam.ems.service.validation.custom.CriteriaConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
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

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    /**
     * Returns all certificates stored in data source
     * @return List of {@link GiftCertificateDto} retrieved from data source
     */
    @GetMapping
    public List<GiftCertificateDto> getAllCerts(){
        return giftCertificateService.getAll();
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
        return giftCertificateService.getById(id);
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
            @RequestBody @CriteriaConstraint Map<String,String> criteria){
        return giftCertificateService.getByCriteria(criteria);
    }

    /**
     * Creates new gift certificate in data source
     * @param toCreate {@link GiftCertificateDto} to create
     */
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void insert(
            @RequestBody @Validated(OnCreate.class) GiftCertificateDto toCreate){
        giftCertificateService.insert(toCreate);
    }

    /**
     * Updates given certificate in data source.
     * @param id ID of entity to update
     * @param toUpdate Entity to update, wrapped in {@link GiftCertificateDto}
     * @throws NoSuchEntityException if there is no such entity in Data Source.
     */
    @PutMapping(value = "/{id}",consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable @Min(value = 1, message = "msg.id.negative") long id,
                                 @RequestBody @Validated(OnUpdate.class) GiftCertificateDto toUpdate)
            throws NoSuchEntityException{
        toUpdate.setId(id);
        giftCertificateService.update(toUpdate);
    }

    /**
     * Removes certificate from Data Source by its ID.
     * @param id ID of certificate to delete.
     * @throws NoSuchEntityException if such certificate does not exist in data source.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Min(value = 1, message = "msg.id.negative") long id) throws NoSuchEntityException{
        giftCertificateService.delete(id);
    }

}
