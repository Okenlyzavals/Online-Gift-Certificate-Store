package com.epam.ems.dao;

import java.util.List;
import java.util.Optional;

public interface AbstractDao<T> {

    Optional<T> retrieveById(long id);

    List<T> retrieveAll();

    T create(T entity);

    void delete(long id);

    void delete(T entity);
}
