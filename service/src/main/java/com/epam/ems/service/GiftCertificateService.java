package com.epam.ems.service;

import com.epam.ems.service.dto.GiftCertificateDto;

import java.util.List;
import java.util.Map;

public interface GiftCertificateService extends AbstractService<GiftCertificateDto> {

    List<GiftCertificateDto> getByCriteria(Map<String, String> criteria);
    void update(GiftCertificateDto entity);

}
