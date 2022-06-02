package com.epam.ems.dao.criteria.parser;

import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.criteria.CertificateCriteria;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OrderParser implements CertificateCriteriaQueryParameterParser<Order> {

    private static final Map<CertificateCriteria.ParamName, OrderFabricator> ORDER_MAP = Map.of(
            CertificateCriteria.ParamName.ORDER_NAME_ASC,
            ((builder, root) -> builder.asc(root.get("name"))),
            CertificateCriteria.ParamName.ORDER_NAME_DESC,
            ((builder, root) -> builder.desc(root.get("name"))),
            CertificateCriteria.ParamName.ORDER_DATE_ASC,
            ((builder, root) -> builder.asc(root.get("createDate"))),
            CertificateCriteria.ParamName.ORDER_DATE_DESC,
            ((builder, root) -> builder.desc(root.get("createDate")))
    );

    @Override
    public Order[] parse(CertificateCriteria criteria, CriteriaBuilder builder, Root<GiftCertificate> root) {
        List<Order> orders = new ArrayList<>();
        criteria.forEach((paramName, o) -> {
            if(ORDER_MAP.containsKey(paramName)){
                orders.add(ORDER_MAP.get(paramName).getOrder(builder, root));
            }
        });
        if (orders.isEmpty()){
            orders.add(builder.asc(root.get("id")));
        }
        return orders.toArray(new Order[0]);
    }

    @FunctionalInterface
    private interface OrderFabricator {
        Order getOrder(CriteriaBuilder builder, Root<GiftCertificate> root);
    }
}
