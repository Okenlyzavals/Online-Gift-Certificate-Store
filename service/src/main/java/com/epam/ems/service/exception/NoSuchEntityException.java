package com.epam.ems.service.exception;

public class NoSuchEntityException extends ServiceException {

    public NoSuchEntityException(Class<?> entityClass) {
        super(entityClass, 404);
        this.errorCode = errorCode*100 + 2;
    }

}
