package com.epam.ems.service.mapper;

import com.epam.ems.dao.entity.Entity;
import com.epam.ems.service.dto.DataTransferObject;

public interface Mapper<E extends Entity, D extends DataTransferObject> {

    D map(E entity);
    E extract(D dto);

}
