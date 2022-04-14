package com.epam.ems.web.controller;

import com.epam.ems.service.GiftCertificateService;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.validation.OnCreate;
import com.epam.ems.service.validation.OnUpdate;
import com.epam.ems.service.validation.custom.CriteriaConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

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

    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> getAllCerts(){
        return new ResponseEntity<>(giftCertificateService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public GiftCertificateDto getCertificate(@PathVariable @Min(value = 1, message = "msg.id.negative") long id){
        return giftCertificateService.getById(id);
    }

    @GetMapping("/by_criteria")
    public List<GiftCertificateDto> getCertificatesByCriteria(
            @RequestBody @CriteriaConstraint Map<String,String> criteria){
        return giftCertificateService.getByCriteria(criteria);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void insert(
            @RequestBody @Validated(OnCreate.class) GiftCertificateDto toCreate){
        giftCertificateService.insert(toCreate);
    }

    @PutMapping(value = "/{id}",consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable @Min(value = 1, message = "msg.id.negative") long id,
                                 @RequestBody @Validated(OnUpdate.class) GiftCertificateDto toUpdate){
        toUpdate.setId(id);
        giftCertificateService.update(toUpdate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Min(value = 1, message = "msg.id.negative") long id){
        giftCertificateService.delete(id);
    }

}
