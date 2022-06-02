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
import java.util.List;

@Component
public abstract class CriteriaQueryBuilder<T> {

    private final CertificateCriteriaQueryParameterParser<Order> orderParser;
    private final CertificateCriteriaQueryParameterParser<Predicate> predicateParser;

    @Autowired
    protected CriteriaQueryBuilder(CertificateCriteriaQueryParameterParser<Order> orderParser,
                                 CertificateCriteriaQueryParameterParser<Predicate> predicateParser) {
        this.orderParser = orderParser;
        this.predicateParser = predicateParser;
    }

    public abstract TypedQuery<T> parse(CertificateCriteria criteria,
                                        EntityManager manager,
                                        Pageable pageable);

    protected CriteriaQuery<T> buildQuery(CriteriaQuery<T> query,
                                                        Root<GiftCertificate> root,
                                                        CriteriaBuilder builder,
                                                        CertificateCriteria criteria){
        return query
                .where(predicateParser.parse(criteria, builder, root))
                .orderBy(orderParser.parse(criteria, builder, root))
                .groupBy(root)
                .having(builder.count(root).in(getCountToGroupBy(criteria)));
    }

    private int getCountToGroupBy(CertificateCriteria criteria){
        if (!criteria.containsKey(CertificateCriteria.ParamName.TAG_NAMES)){
            return 1;
        }
        return ((List<String>)criteria.get(CertificateCriteria.ParamName.TAG_NAMES)).size();
    }
}
