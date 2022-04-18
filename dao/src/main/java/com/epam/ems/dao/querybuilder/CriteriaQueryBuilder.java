package com.epam.ems.dao.querybuilder;

import com.epam.ems.dao.constant.DBMetadata;
import com.epam.ems.dao.entity.criteria.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CriteriaQueryBuilder{

    private static final String SQL_SELECT = String.format("SELECT * FROM %s",
            DBMetadata.CERTIFICATES_TABLE);
    private static final String SELECT_BY_TAG = String.format(
            "SELECT * FROM %s " +
                    "RIGHT JOIN %s ON %s = %s " +
                    "LEFT JOIN %s ON %s=%s",
            DBMetadata.CERTIFICATES_TABLE, DBMetadata.CERT_HAS_TAG_TABLE,
            DBMetadata.CERTIFICATES_TABLE_ID, DBMetadata.CERT_HAS_TAG_TABLE_ID_CERT,
            DBMetadata.TAG_TABLE, DBMetadata.TAG_TABLE_ID,
            DBMetadata.CERT_HAS_TAG_TABLE_ID_TAG);
    private static final String GROUP_BY = String.format(
            " GROUP BY %s", DBMetadata.CERTIFICATES_TABLE_ID);

    private static final  Map<Criteria.ParamName, String[]> orderTypes = Map.of(
            Criteria.ParamName.ORDER_DATE_ASC, new String[]{"ASC",DBMetadata.CERTIFICATES_TABLE_CREATED},
            Criteria.ParamName.ORDER_DATE_DESC, new String[]{"DESC",DBMetadata.CERTIFICATES_TABLE_CREATED},
            Criteria.ParamName.ORDER_NAME_ASC, new String[]{"ASC",DBMetadata.CERTIFICATES_TABLE_NAME},
            Criteria.ParamName.ORDER_NAME_DESC, new String[]{"DESC",DBMetadata.CERTIFICATES_TABLE_NAME});

    private final List<Object> params = new ArrayList<>();
    private final StringBuilder sqlBuilder = new StringBuilder();
    private final StringBuilder orderBuilder = new StringBuilder();
    private final StringBuilder conditionBuilder = new StringBuilder();
    private Criteria criteria;

    public void parse(Criteria entity) {
        params.clear();
        orderBuilder.setLength(0);
        conditionBuilder.setLength(0);
        sqlBuilder.setLength(0);
        criteria = entity;
        buildQuery();
    }

    public Object[] getParams() {
        return params.toArray();
    }

    public String getQuery() {
        return sqlBuilder.toString();
    }

    void buildQuery(){
        String tagName = criteria.get(Criteria.ParamName.TAG_NAME);

        parseOrder();
        parsePartNameDescConditions();
        parseTagName();

        sqlBuilder.append(conditionBuilder);

        if (tagName != null){
            sqlBuilder.append(GROUP_BY);
        }
        sqlBuilder.append(orderBuilder);
    }

    private void parseTagName(){
        String tagName = criteria.get(Criteria.ParamName.TAG_NAME);

        if(tagName != null){
            sqlBuilder.append(SELECT_BY_TAG);
            if (params.isEmpty()){
                conditionBuilder.append(" WHERE ");
            } else {
                conditionBuilder.append(" AND ");
            }
            conditionBuilder
                    .append(DBMetadata.TAG_TABLE_NAME)
                    .append(" LIKE ")
                    .append("?");
            params.add(tagName);
        } else {
            sqlBuilder.append(SQL_SELECT);
        }

    }

    private void parsePartNameDescConditions(){

        String namePart = criteria.get(Criteria.ParamName.NAME_CONTAINS);
        String descPart = criteria.get(Criteria.ParamName.DESCRIPTION_CONTAINS);

        if(namePart != null || descPart != null){
            String conjunction;
            if(namePart != null && descPart != null){
                conjunction = " OR ";
            } else if (namePart != null){
                conjunction = " AND ";
                descPart = "";
            } else {
                conjunction = " AND ";
                namePart = "";
            }

            conditionBuilder
                    .append(" WHERE ((")
                    .append(DBMetadata.CERTIFICATES_TABLE_NAME)
                    .append(" LIKE ")
                    .append("?")
                    .append(namePart.isEmpty() ? " OR "+ DBMetadata.CERTIFICATES_TABLE_NAME + " IS NULL": "")
                    .append(")")
                    .append(conjunction)
                    .append("(")
                    .append(DBMetadata.CERTIFICATES_TABLE_DESC)
                    .append(" LIKE ")
                    .append("?")
                    .append(descPart.isEmpty() ? " OR "+ DBMetadata.CERTIFICATES_TABLE_DESC + " IS NULL": "")
                    .append("))");
            params.add("%"+namePart+"%");
            params.add("%"+descPart+"%");
        }
    }

    private void parseOrder(){
        boolean isFull = false;

        for(Criteria.ParamName order : criteria.keySet()){
            if(orderTypes.containsKey(order)){
                orderBuilder
                        .append(isFull ? ", " : " ORDER BY ")
                        .append(orderTypes.get(order)[1])
                        .append(" ")
                        .append(orderTypes.get(order)[0]);
                isFull = true;
            }
        }
    }

}
