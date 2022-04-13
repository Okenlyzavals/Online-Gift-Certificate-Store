package com.epam.ems.service.exception;

import lombok.Getter;

@Getter
public class DuplicateEntityException extends ServiceException{

    private final long id;

    public DuplicateEntityException(long id, Class<?> entityClass) {
        super(entityClass, 400);
        this.id=id;
        this.errorCode = errorCode*100 + 1;
    }

}
