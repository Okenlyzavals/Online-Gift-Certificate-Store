package com.epam.ems.dao.impl;

import com.epam.ems.dao.OrderDao;
import com.epam.ems.dao.entity.Order;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDaoImpl implements OrderDao {

    @PersistenceContext
    public EntityManager manager;

    @Override
    public Optional<Order> retrieveById(long id) {
        return Optional.ofNullable(manager.find(Order.class, id));
    }

    @Override
    public List<Order> retrieveAll(int page, int elements) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();

        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root);
        query.orderBy(builder.asc(root.get("id")));

        TypedQuery<Order> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult((page-1)*elements);
        typedQuery.setMaxResults(elements);
        return typedQuery.getResultList();
    }

    @Override
    @Transactional
    public Order create(Order entity) {
        manager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public void delete(long id) {
        Order toDelete = retrieveById(id).orElseThrow(IllegalArgumentException::new);
        manager.remove(toDelete);
    }

    @Override
    public List<Order> retrieveByUserId(Long id, int page, int elements) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();

        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root);
        query.where(builder.equal(root.get("user").get("id"), id));
        query.orderBy(builder.asc(root.get("id")));

        TypedQuery<Order> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult((page-1)*elements);
        typedQuery.setMaxResults(elements);
        return typedQuery.getResultList();
    }
}
