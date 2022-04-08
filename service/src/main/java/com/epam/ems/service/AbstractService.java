package com.epam.ems.service;

import com.epam.ems.service.dto.DataTransferObject;

import java.util.List;

public interface AbstractService<T extends DataTransferObject> {

    T getById(Long id);
    List<T> getAll();
    void insert(T entity);
    void delete(Long id);
    void delete(T entity);

}
