package com.epam.ems.web.controller;

import com.epam.ems.model.criteria.Criteria;
import com.epam.ems.service.GiftCertificateService;
import com.epam.ems.service.dto.GiftCertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public GiftCertificateDto getCertificate(@PathVariable long id){
        return giftCertificateService.getById(id);
    }

    @GetMapping("/by_criteria")
    public List<GiftCertificateDto> getCertificatesByCriteria(@RequestBody Criteria criteria){
        return giftCertificateService.getByCriteria(criteria);
    }

    @PostMapping(value = "/new",consumes = "application/json")
    public ResponseEntity insert(@RequestBody GiftCertificateDto toCreate){
        giftCertificateService.insert(toCreate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}",consumes = "application/json")
    public ResponseEntity insert(@PathVariable long id, @RequestBody GiftCertificateDto toUpdate){
        toUpdate.setId(id);
        giftCertificateService.update(toUpdate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable long id){
        giftCertificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
