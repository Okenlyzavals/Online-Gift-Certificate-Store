package com.epam.ems.dao.custom;

import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.criteria.CertificateCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftCertificateDaoCustom {
    Page<GiftCertificate> retrieveByCriteria(CertificateCriteria criteria, Pageable pageable);
}
