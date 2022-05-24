package com.epam.ems.service.exception;

import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Order;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.dao.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * General exception class for Service layer.
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
@Getter
@Setter
public abstract class ServiceException extends RuntimeException {
    private static final Map<Class<?>, Integer> ENTITY_ERROR_CODES = Map.of(
            GiftCertificate.class, 1,
            Tag.class, 2,
            User.class, 3,
            Order.class, 4);
    protected int errorCode;

    public ServiceException(Class<?> entityClass, int errorCode){
        this.errorCode = (errorCode * 100) + (ENTITY_ERROR_CODES.get(entityClass));
    }
}
