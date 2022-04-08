package com.epam.ems.dao;

import com.epam.ems.dao.entity.Entity;

import java.util.List;
import java.util.Optional;

public interface AbstractDao<T extends Entity> {

    Optional<T> retrieveById(long id);

    List<T> retrieveAll();

    Long create(T entity);

    void delete(long id);

    void delete(T entity);
}
