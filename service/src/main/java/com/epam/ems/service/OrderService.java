package com.epam.ems.service;

import com.epam.ems.service.dto.OrderDto;
import com.epam.ems.service.exception.NoSuchEntityException;

import java.util.List;

/**
 * Interface extending {@link AbstractService}, suited for use with
 * instances of {@link OrderDto}
 *
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
public interface OrderService extends AbstractService<OrderDto> {

    /**
     * Retrieves list of orders from data source
     * that were made by user with given ID.
     * @param id ID of user to retrieve orders from.
     * @param page Page to start displaying from.
     * @param elements Number of elements per page.
     * @return  List of {@link OrderDto} instances (trimmed to fit page & elements)
     * that were made by user with given ID.
     * @throws NoSuchEntityException If there is no user with such ID.
     */
    List<OrderDto> getOrdersByUser(Long id, int page, int elements) throws NoSuchEntityException;

}
