package com.epam.ems.dao;

import java.util.List;
import java.util.Optional;

/**
 * An interface for DAO objects, providing support for CRD operations.
 * @param <T> type of entity that implementation works with.
 *
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
public interface AbstractDao<T> {

    /**
     * Retrieves entity by its ID.
     * @param id ID of entity in data source.
     * @return  {@link Optional} of entity retrieved from data source.
     * If no entity was found, returns empty {@link Optional}
     */
    Optional<T> retrieveById(long id);

    /**
     * Retrieves all entities of given type from data source.
     * @param page Page to start from.
     * @param elements Number of elements per page.
     * @return  List of all entities (trimmed to fit page & elements)
     * of given type retrieved from data source.
     */
    List<T> retrieveAll(int page, int elements);

    /**
     * Saves entity to data source.
     * @param entity Entity to save.
     * @return Created entity.
     */
    T create(T entity);

    /**
     * Removed entity from data source.
     * @param id ID of entity to delete.
     */
    void delete(long id);
}
