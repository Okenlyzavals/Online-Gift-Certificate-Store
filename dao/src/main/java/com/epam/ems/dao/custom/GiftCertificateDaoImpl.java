package com.epam.ems.dao.custom;

import com.epam.ems.dao.criteria.CriteriaQueryBuilder;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.criteria.CertificateCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDaoCustom{

    private final CriteriaQueryBuilder<GiftCertificate> entityQueryBuilder;
    private final CriteriaQueryBuilder<Long> entityCountQueryBuilder;

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    public GiftCertificateDaoImpl(CriteriaQueryBuilder<GiftCertificate> entityQueryBuilder,
                                  CriteriaQueryBuilder<Long> entityCountQueryBuilder) {
        this.entityQueryBuilder = entityQueryBuilder;
        this.entityCountQueryBuilder = entityCountQueryBuilder;
    }

    @Override
    public Page<GiftCertificate> retrieveByCriteria(CertificateCriteria criteria, Pageable pageable) {
        List<GiftCertificate> results = entityQueryBuilder.parse(criteria, manager, pageable).getResultList();
        Long totalSize = entityCountQueryBuilder.parse(criteria, manager, pageable).getSingleResult();
        return new PageImpl<>(results, pageable, totalSize);
    }
}
