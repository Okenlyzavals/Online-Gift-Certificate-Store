package com.epam.ems.dao.criteria;

import com.epam.ems.dao.criteria.parser.CertificateCriteriaQueryParameterParser;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.criteria.CertificateCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

@Component
public class CriteriaQueryForCount extends CriteriaQueryBuilder<Long> {

    @Autowired
    protected CriteriaQueryForCount(CertificateCriteriaQueryParameterParser<Order> orderParser, CertificateCriteriaQueryParameterParser<Predicate> predicateParser) {
        super(orderParser, predicateParser);
    }

    @Override
    public TypedQuery<Long> parse(CertificateCriteria criteria, EntityManager manager, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);

        query = buildQuery(query, root, builder, criteria)
                .select(builder.count(root));

        return  manager.createQuery(query);
    }


}
