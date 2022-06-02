package com.epam.ems.service;

import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.exception.UpdateException;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * Interface extending {@link AbstractService}, suited for use with
 * instances of {@link GiftCertificateDto}
 *
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
public interface GiftCertificateService extends AbstractService<GiftCertificateDto> {

    /**
     * Retrieves list of gift certificates from data source
     * by map of filter configuration parameters.
     *
     * @param criteria A map containing filter as key and value to filter by as value.
     * @param page Page to start display from.
     * @param elements Number of elements per page.
     * @return List of {@link GiftCertificateDto} instances (trimmed to fit page & elements)
     * matching given criteria.
     */
    Page<GiftCertificateDto> getByCriteria(Map<String, Object> criteria, int page, int elements);

    /**
     * Updates given certificate in Data Source.
     * @param entity Entity to update, wrapped in {@link GiftCertificateDto}
     * @throws NoSuchEntityException if there is no such entity in Data Source.
     * @throws UpdateException if an error has occurred during update process.
     */
    GiftCertificateDto update(GiftCertificateDto entity) throws NoSuchEntityException, UpdateException;

}
