package com.epam.ems.dao.custom;

import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.criteria.CertificateCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Custom extension for {@link com.epam.ems.dao.GiftCertificateDao}.
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
@Repository
public interface GiftCertificateDaoCustom {

    /**
     * Retrieves list of gift certificates from data source
     * by {@link CertificateCriteria} - a map of filters parameters.
     * @param criteria CertificateCriteria object.
     * @param pageable Pagination information.
     * @return Page of {@link GiftCertificate} instances
     * matching given criteria.
     */
    Page<GiftCertificate> retrieveByCriteria(CertificateCriteria criteria, Pageable pageable);
}
