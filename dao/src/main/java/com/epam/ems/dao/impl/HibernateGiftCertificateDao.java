package com.epam.ems.dao.impl;

import com.epam.ems.dao.GiftCertificateDao;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.criteria.Criteria;
import com.epam.ems.dao.querybuilder.CriteriaQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

@Repository
@ComponentScan({"com.epam.ems.dao"})
public class HibernateGiftCertificateDao implements GiftCertificateDao {

    private final CriteriaQueryBuilder criteriaQueryBuilder;

    @PersistenceContext
    public EntityManager manager;


    @Autowired
    public HibernateGiftCertificateDao(CriteriaQueryBuilder criteriaQueryBuilder){
        this.criteriaQueryBuilder = criteriaQueryBuilder;
    }

    @Override
    public Optional<GiftCertificate> retrieveById(long id) {
        return Optional.ofNullable(manager.find(GiftCertificate.class, id));
    }

    @Override
    public List<GiftCertificate> retrieveAll(int page, int elements) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);

        TypedQuery<GiftCertificate> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult((page-1)*elements);
        typedQuery.setMaxResults(elements);

        return typedQuery.getResultList();
    }

    @Override
    public GiftCertificate create(GiftCertificate entity) {
        manager.persist(entity);
        return entity;
    }

    @Override
    public void delete(long id) {
        GiftCertificate toDelete = retrieveById(id).orElseThrow();
        manager.remove(toDelete);
    }

    @Override
    public List<GiftCertificate> retrieveByCriteria(Criteria criteria, int page, int elements){
        return criteriaQueryBuilder.parse(criteria, manager, page,elements).getResultList();
    }

    @Override
    public GiftCertificate update(GiftCertificate entity) {
        manager.merge(entity);
        return entity;
    }
}
