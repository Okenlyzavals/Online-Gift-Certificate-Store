package com.epam.ems.service;

import com.epam.ems.service.dto.DataTransferObject;
import com.epam.ems.service.exception.DuplicateEntityException;
import com.epam.ems.service.exception.NoSuchEntityException;

import java.util.List;

/**
 * An interface for service objects, providing support for CRD operations.
 * @param <T> has to implement {@link DataTransferObject},
 *          as it is used to communicate with controller.
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
public interface AbstractService<T extends DataTransferObject> {

    /**
     * Retrieves single entity from Data Source.
     * @param id ID of and entity to retrieve.
     * @return Requested entity wrapped in corresponding Data Transfer Object.
     * @throws NoSuchEntityException if no entity was found.
     */
    T getById(Long id) throws NoSuchEntityException;

    /**
     * Retrieves all entities stored in Data Source.
     * @param page Page to browse.
     * @param elements Number of elements per page.
     * @return List of all entities (trimmed to fit page & elements) retrieved from Data Source,
     * each one being wrapped in corresponding DTO.
     */
    List<T> getAll(int page, int elements);

    /**
     * Saves entity to Data Source.
     * @param entity Entity to save, wrapped in corresponding DTO.
     * @throws DuplicateEntityException if such entity already exists.
     */
    T insert(T entity) throws DuplicateEntityException;

    /**
     * Removes entity from Data Source by its ID.
     * @param id ID of entity to delete.
     * @throws NoSuchEntityException if such entity does not exist in data source.
     */
    void delete(Long id) throws NoSuchEntityException;
    /**
     * Removes entity from Data Source.
     * @param entity Entity to delete wrapped in corresponding DTO.
     * @throws NoSuchEntityException if such entity does not exist in data source.
     */
    void delete(T entity) throws NoSuchEntityException;

}
