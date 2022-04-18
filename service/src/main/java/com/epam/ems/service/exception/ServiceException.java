package com.epam.ems.service.exception;

import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public abstract class ServiceException extends RuntimeException {
    private static final Map<Class<?>, Integer> ENTITY_ERROR_CODES = Map.of(
            GiftCertificate.class, 1,
            Tag.class, 2);
    protected int errorCode;

    public ServiceException(Class<?> entityClass, int errorCode){
        this.errorCode = (errorCode * 100) + (ENTITY_ERROR_CODES.get(entityClass));
    }
}
