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

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping(value = "/certificates")
@ComponentScan("com.epam.ems.service.impl")
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
    public GiftCertificateDto getCertificate(@PathVariable @Min(1) long id){
        return giftCertificateService.getById(id);
    }

    @GetMapping("/by_criteria")
    public List<GiftCertificateDto> getCertificatesByCriteria(
            @RequestBody @CriteriaConstraint Map<String,String> criteria){
        return giftCertificateService.getByCriteria(criteria);
    }

    @PostMapping(value = "/new",consumes = "application/json")
    public ResponseEntity insert(
            @RequestBody @Validated(OnCreate.class) GiftCertificateDto toCreate){
        giftCertificateService.insert(toCreate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}",consumes = "application/json")
    public ResponseEntity update(@PathVariable @Min(1) long id,
                                 @RequestBody @Validated(OnUpdate.class) GiftCertificateDto toUpdate){
        toUpdate.setId(id);
        giftCertificateService.update(toUpdate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable @Min(1) long id){
        giftCertificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

}
