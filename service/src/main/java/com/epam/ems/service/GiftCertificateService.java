package com.epam.ems.service;

import com.epam.ems.model.criteria.Criteria;
import com.epam.ems.service.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService extends AbstractService<GiftCertificateDto> {

    List<GiftCertificateDto> getByCriteria(Criteria criteria);
    void update(GiftCertificateDto entity);

}
