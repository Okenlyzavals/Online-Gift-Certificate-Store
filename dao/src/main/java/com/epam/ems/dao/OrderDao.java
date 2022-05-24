package com.epam.ems.dao;

import com.epam.ems.dao.entity.Order;

import java.util.List;

/**
 * Extension of {@link AbstractDao} suited for {@link Order} entities.
 *
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
public interface OrderDao extends AbstractDao<Order> {

    /**
     * Retrieves list of orders from data source
     * that were made by user with given ID.
     * @param id ID of user to retrieve orders from.
     * @param page Page to start displaying from.
     * @param elements Number of elements per page.
     * @return  List of {@link Order} instances (trimmed to fit page & elements)
     * that were made by user with given ID.
     */
    List<Order> retrieveByUserId(Long id, int page, int elements);
}
