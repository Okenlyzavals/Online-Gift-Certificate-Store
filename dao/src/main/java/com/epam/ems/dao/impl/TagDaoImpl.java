package com.epam.ems.dao.impl;

import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.entity.Tag;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Optional<Tag> retrieveById(long id) {
        return Optional.ofNullable(manager.find(Tag.class, id));
    }

    @Override
    public List<Tag> retrieveAll(int page, int elements) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();

        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);
        query.select(root);
        query.orderBy(builder.asc(root.get("id")));

        TypedQuery<Tag> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult((page-1)*elements);
        typedQuery.setMaxResults(elements);

        return typedQuery.getResultList();
    }

    @Override
    @Transactional
    public Tag create(Tag entity) {
        manager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public void delete(long id) {
        Tag toDelete = retrieveById(id).orElseThrow(IllegalArgumentException::new);
        manager.remove(toDelete);
    }


    @Override
    public Optional<Tag> findByName(String name) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();

        CriteriaQuery<Tag> tagCriteria = builder.createQuery(Tag.class);
        Root<Tag> tagRoot = tagCriteria.from(Tag.class);
        tagCriteria.select(tagRoot);
        tagCriteria.where(builder.equal(tagRoot.get("name"), name));

        Optional<Tag> result;

        try{
            result = Optional.of(manager.createQuery(tagCriteria).getSingleResult());
        } catch (NoResultException e){
            result = Optional.empty();
        }

        return result;

    }

    @Override
    public Optional<Tag> findMostUsedTagOfUserWithHighestOrderCost() {
        StoredProcedureQuery query = manager.createNamedStoredProcedureQuery("GetMostUsedTag");
        return Optional.ofNullable((Tag) query.getSingleResult());
    }
}
