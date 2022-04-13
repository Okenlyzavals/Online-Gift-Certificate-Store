package com.epam.ems.service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ServiceException extends RuntimeException {
    protected int errorCode;

    public ServiceException(Class<?> entityClass, int errorCode){
        this.errorCode = (errorCode * 100) + (entityClass.hashCode() % 100);
    }
}
