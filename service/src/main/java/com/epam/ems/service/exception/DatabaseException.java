package com.epam.ems.service.exception;

public class DatabaseException extends ServiceException{
    public DatabaseException(Class<?> entityClass) {
        super(entityClass, 500);
        this.errorCode=this.errorCode * 100 + 3;
    }
}
