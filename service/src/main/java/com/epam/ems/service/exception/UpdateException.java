package com.epam.ems.service.exception;

public class UpdateException extends ServiceException{
    public UpdateException(Class<?> entityClass) {
        super(entityClass, 40003);
    }
}
