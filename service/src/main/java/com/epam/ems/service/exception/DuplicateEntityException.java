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
        super(entityClass, 40001);
        this.id=id;
    }

}
