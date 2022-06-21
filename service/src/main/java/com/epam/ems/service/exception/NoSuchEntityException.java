package com.epam.ems.service.exception;

/**
 * Extension of {@link ServiceException}
 * used to show that entity does not exist in data source.
 *
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
public class NoSuchEntityException extends ServiceException {

    public NoSuchEntityException(Class<?> entityClass) {
        super(entityClass, 40402);
    }

}
