package com.epam.ems.dao.criteria.parser;

import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.criteria.CertificateCriteria;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PredicateParser implements CertificateCriteriaQueryParameterParser<Predicate> {

    private static final Map<CertificateCriteria.ParamName, PredicateFabricator> PREDICATE_MAP = Map.of(
            CertificateCriteria.ParamName.TAG_NAMES,
            (builder, root, parameter) ->
                    root.joinSet("tags").get("name").in((List<String>) parameter),
            CertificateCriteria.ParamName.NAME_CONTAINS,
            (builder, root, parameter) ->
                    builder.like(root.get("name"), "%" + parameter + "%"),
            CertificateCriteria.ParamName.DESCRIPTION_CONTAINS,
            (builder, root, parameter) ->
                    builder.like(root.get("description"), "%" + parameter + "%")
    );

    @Override
    public Predicate[] parse(CertificateCriteria criteria, CriteriaBuilder builder, Root<GiftCertificate> root) {
        List<Predicate> predicates = new ArrayList<>();
        criteria.forEach((paramName, o) -> {
            if(PREDICATE_MAP.containsKey(paramName)){
                predicates.add(PREDICATE_MAP.get(paramName).getPredicate(builder, root, o));
            }
        });
        return predicates.toArray(new Predicate[0]);
    }


    @FunctionalInterface
    private interface PredicateFabricator {
        Predicate getPredicate(CriteriaBuilder builder, Root<GiftCertificate> root, Object parameter);
    }
}
