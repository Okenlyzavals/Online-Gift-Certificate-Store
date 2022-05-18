package com.epam.ems.dao.querybuilder;

import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.dao.entity.criteria.Criteria;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class CriteriaQueryBuilder{

    private static final Map<Criteria.ParamName, PredicateFabricator> PREDICATE_MAP = Map.of(
            Criteria.ParamName.TAG_NAMES,
            (builder, root, parameter) ->
                    root.joinSet("tags").get("name").in((List<String>) parameter),
            Criteria.ParamName.NAME_CONTAINS,
            (builder, root, parameter) ->
                    builder.like(root.get("name"), "%" + parameter + "%"),
            Criteria.ParamName.DESCRIPTION_CONTAINS,
            (builder, root, parameter) ->
                    builder.like(root.get("description"), "%" + parameter + "%")
    );

    private static final Map<Criteria.ParamName, OrderFabricator> ORDER_MAP = Map.of(
            Criteria.ParamName.ORDER_NAME_ASC,
            ((builder, root) -> builder.asc(root.get("name"))),
            Criteria.ParamName.ORDER_NAME_DESC,
            ((builder, root) -> builder.desc(root.get("name"))),
            Criteria.ParamName.ORDER_DATE_ASC,
            ((builder, root) -> builder.asc(root.get("createDate"))),
            Criteria.ParamName.ORDER_DATE_DESC,
            ((builder, root) -> builder.desc(root.get("createDate")))
    );

    public TypedQuery<GiftCertificate> parse(Criteria criteria, EntityManager manager, int page, int elements){

        TypedQuery<GiftCertificate> result = manager.createQuery(buildQuery(manager,criteria));
        result.setFirstResult((page-1)*elements);
        result.setMaxResults(elements);
        return result;
    }

    private CriteriaQuery<GiftCertificate> buildQuery(final EntityManager manager,
                                                      final Criteria criteria){
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query
                .select(root)
                .where(getPredicates(criteria, builder, root))
                .orderBy(getOrders(criteria, builder, root))
                .groupBy(root)
                .having(builder.count(root).in(getCountToGroupBy(criteria)));

        return query;
    }

    private int getCountToGroupBy(Criteria criteria){
        if (!criteria.containsKey(Criteria.ParamName.TAG_NAMES)){
            return 1;
        }
        return ((List<String>)criteria.get(Criteria.ParamName.TAG_NAMES)).size();
    }

    private Predicate[] getPredicates(Criteria criteria, CriteriaBuilder builder, Root<GiftCertificate> root){
        List<Predicate> predicates = new ArrayList<>();
        criteria.forEach((paramName, o) -> {
            if(PREDICATE_MAP.containsKey(paramName)){
                predicates.add(PREDICATE_MAP.get(paramName).getPredicate(builder, root, o));
            }
        });
        return predicates.toArray(new Predicate[0]);
    }

    private Order[] getOrders(Criteria criteria, CriteriaBuilder builder, Root<GiftCertificate> root){
        List<Order> orders = new ArrayList<>();
        criteria.forEach((paramName, o) -> {
            if(ORDER_MAP.containsKey(paramName)){
                orders.add(ORDER_MAP.get(paramName).getOrder(builder, root));
            }
        });
        return orders.toArray(new Order[0]);
    }

    @FunctionalInterface
    private interface PredicateFabricator {
        Predicate getPredicate(CriteriaBuilder builder, Root<GiftCertificate> root, Object parameter);
    }

    @FunctionalInterface
    private interface OrderFabricator {
        Order getOrder(CriteriaBuilder builder, Root<GiftCertificate> root);
    }

}
