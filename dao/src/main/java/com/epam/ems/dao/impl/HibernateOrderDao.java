package com.epam.ems.dao.impl;

import com.epam.ems.dao.OrderDao;
import com.epam.ems.dao.entity.Order;
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
public class HibernateOrderDao implements OrderDao {

    @PersistenceContext
    public EntityManager manager;

    @Override
    public Optional<Order> retrieveById(long id) {
        return Optional.ofNullable(manager.find(Order.class, id));
    }

    @Override
    public List<Order> retrieveAll(int page, int elements) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root);

        TypedQuery<Order> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult((page-1)*elements);
        typedQuery.setMaxResults(elements);
        return typedQuery.getResultList();
    }

    @Override
    public Order create(Order entity) {
        manager.persist(entity);
        return entity;
    }

    @Override
    public void delete(long id) {
        Order toDelete = retrieveById(id).orElseThrow();
        manager.remove(toDelete);
    }
}
