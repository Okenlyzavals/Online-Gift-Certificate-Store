package com.epam.ems.service.mapper;

import com.epam.ems.service.dto.DataTransferObject;

public interface Mapper<E, D extends DataTransferObject> {

    D map(E entity);
    E extract(D dto);

}
