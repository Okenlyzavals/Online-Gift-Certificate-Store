package com.epam.ems.dao.impl;

import com.epam.ems.dao.UserDao;
import com.epam.ems.dao.entity.Order;
import com.epam.ems.dao.entity.User;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@ComponentScan("com.epam.ems.dao")
public class HibernateUserDao implements UserDao {

    @PersistenceContext
    public EntityManager manager;

    @Override
    public Optional<User> retrieveById(long id) {
        return Optional.ofNullable(manager.find(User.class, id));
    }

    @Override
    public List<User> retrieveAll(int page, int elements) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);

        TypedQuery<User> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult((page-1)*elements);
        typedQuery.setMaxResults(elements);
        return typedQuery.getResultList();
    }

    @Override
    public User create(User entity) {
        manager.persist(entity);
        return entity;
    }

    @Override
    public void delete(long id) {
        throw new UnsupportedOperationException();
    }
}
