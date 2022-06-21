package com.epam.ems.dao;

import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.criteria.Criteria;

import java.util.List;

/**
 * Extension of {@link AbstractDao} suited for {@link GiftCertificate} entities.
 *
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
public interface GiftCertificateDao extends AbstractDao<GiftCertificate> {

    /**
     * Retrieves list of gift certificates from data source
     * by {@link Criteria} - a map of filters parameters.
     * @param criteria Criteria object.
     * @param page Page to start display from.
     * @param elements Number of elements per page.
     * @return List of {@link GiftCertificate} instances (trimmed to fit page & elements)
     * matching given criteria.
     */
    List<GiftCertificate> retrieveByCriteria(Criteria criteria, int page, int elements);

    /**
     * Updates certificate in data source.
     * @param entity Certificate to update.
     * @return Updated instance of {@link GiftCertificate}
     */
    GiftCertificate update(GiftCertificate entity);
}
