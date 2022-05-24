package com.epam.ems.service.mapper;

import com.epam.ems.service.dto.DataTransferObject;

/**
 * Interface that specifies behavior for DTO mapper objects.
 * @param <E> Type of entity to map to/extracted from DTO.
 * @param <D> Type of DTO to map entity to/extract entity from.
 */
public interface Mapper<E, D extends DataTransferObject> {

    /**
     * Maps Entity tu DTO.
     * @param entity Entity to wrap.
     * @return DTO containing data of entity.
     */
    D map(E entity);

    /**
     * Extracts Entity from DTO.
     * @param dto DTO to extract data from.
     * @return Entity containing data extracted from DTO.
     */
    E extract(D dto);

}
