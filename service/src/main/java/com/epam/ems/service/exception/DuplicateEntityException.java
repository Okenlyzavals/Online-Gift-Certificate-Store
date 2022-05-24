package com.epam.ems.service.exception;

import lombok.Getter;

/**
 * Extension of {@link ServiceException}
 * used to show that entity is already present in data source.
 *
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
@Getter
public class DuplicateEntityException extends ServiceException{

    private final long id;

    public DuplicateEntityException(long id, Class<?> entityClass) {
        super(entityClass, 400);
        this.id=id;
        this.errorCode = errorCode*100 + 1;
    }

}
