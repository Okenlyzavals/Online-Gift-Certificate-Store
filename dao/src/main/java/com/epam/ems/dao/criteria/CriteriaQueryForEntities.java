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
public class CriteriaQueryForEntities extends CriteriaQueryBuilder<GiftCertificate> {

    @Autowired
    protected CriteriaQueryForEntities(CertificateCriteriaQueryParameterParser<Order> orderParser,
                                       CertificateCriteriaQueryParameterParser<Predicate> predicateParser) {
        super(orderParser, predicateParser);
    }

    public TypedQuery<GiftCertificate> parse(CertificateCriteria criteria, EntityManager manager, Pageable pageable){

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);

        query = buildQuery(query, root, builder, criteria)
                .select(root);

        TypedQuery<GiftCertificate> result = manager.createQuery(query);
        result.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
        result.setMaxResults(pageable.getPageSize());
        return result;

    }

}
