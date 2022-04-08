package com.epam.ems.dao.impl;

import com.epam.ems.dao.GiftCertificateDao;
import com.epam.ems.dao.constant.DBMetadata;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.dao.rowmapper.CertificateRowMapper;
import com.epam.ems.dao.rowmapper.TagRowMapper;
import com.epam.ems.model.criteria.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

@Repository
@ComponentScan("com.epam.ems.dao")
public class SqlGiftCertificateDao implements GiftCertificateDao {

    private static final String SQL_SELECT = String.format("SELECT * FROM %s",
            DBMetadata.CERTIFICATES_TABLE);
    private static final String SQL_SELECT_ID = String.format("%s WHERE %s=?",
            SQL_SELECT, DBMetadata.CERTIFICATES_TABLE_ID);
    private static final String SQL_INSERT = String.format(
            "INSERT INTO %s" +
            " (%s,%s,%s,%s,%s,%s,%s)" +
            " VALUES (DEFAULT,?,?,?,?,?,?);",
            DBMetadata.CERTIFICATES_TABLE,DBMetadata.CERTIFICATES_TABLE_ID,
            DBMetadata.CERTIFICATES_TABLE_NAME, DBMetadata.CERTIFICATES_TABLE_DESC,
            DBMetadata.CERTIFICATES_TABLE_PRICE, DBMetadata.CERTIFICATES_TABLE_DURATION,
            DBMetadata.CERTIFICATES_TABLE_CREATED, DBMetadata.CERTIFICATES_TABLE_LAST_UPDATE);
    private static final String SQL_DELETE = String.format("DELETE FROM %s WHERE %s=?",
            DBMetadata.CERTIFICATES_TABLE, DBMetadata.CERTIFICATES_TABLE_ID);

    private static final String SQL_SELECT_TAGS = String.format(
            "SELECT * FROM %s " +
            "LEFT JOIN %s " +
            "ON %s=%s WHERE %s=?",
            DBMetadata.CERT_HAS_TAG_TABLE, DBMetadata.TAG_TABLE,
            DBMetadata.TAG_TABLE_ID, DBMetadata.CERT_HAS_TAG_TABLE_ID_TAG,
            DBMetadata.CERT_HAS_TAG_TABLE_ID_CERT);
    private static final String SQL_CREATE_TAG_LINK = String.format(
            "INSERT INTO %s (%s,%s) VAlUES(?,?)",
            DBMetadata.CERT_HAS_TAG_TABLE, DBMetadata.CERT_HAS_TAG_TABLE_ID_CERT,
            DBMetadata.CERT_HAS_TAG_TABLE_ID_TAG);
    private static final String SQL_REMOVE_TAG_LINK = String.format(
            "DELETE FROM %s WHERE %s=? AND %s=?",
            DBMetadata.CERT_HAS_TAG_TABLE, DBMetadata.CERT_HAS_TAG_TABLE_ID_CERT,
            DBMetadata.CERT_HAS_TAG_TABLE_ID_TAG);


    private final JdbcTemplate template;
    private CertificateRowMapper mapper;

    @Autowired
    public SqlGiftCertificateDao(@Qualifier(value = "mySql")DataSource dataSource,
                                 CertificateRowMapper mapper){
        template = new JdbcTemplate(dataSource);
        this.mapper = mapper;
    }

    @Override
    public Optional<GiftCertificate> retrieveById(long id) {
        Optional<GiftCertificate> certificate =
                template.query(SQL_SELECT_ID, mapper, id).stream().findAny();
        certificate.ifPresent(giftCertificate -> giftCertificate.setTags(retrieveTags(id)));
        return certificate;
    }

    @Override
    public List<GiftCertificate> retrieveAll() {
        List<GiftCertificate> certs = template.query(SQL_SELECT, mapper);
        for(GiftCertificate certificate : certs){
            certificate.setTags(retrieveTags(certificate.getId()));
        }
        return certs;
    }

    @Override
    public Long create(GiftCertificate entity) {
        KeyHolder holder = new GeneratedKeyHolder();

        template.update(con -> {
            PreparedStatement statement = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1,entity.getName());
            statement.setObject(2,entity.getDescription());
            statement.setObject(3,entity.getPrice());
            statement.setObject(4,entity.getDuration());
            statement.setObject(5, Timestamp.valueOf(entity.getCreateDate()));
            statement.setObject(6, Timestamp.valueOf(entity.getLastUpdateDate()));
            return statement;
        },holder);
        Long key = holder.getKey().longValue();
        for(Tag tag : entity.getTags()){
            template.update(SQL_CREATE_TAG_LINK, key, tag.getId());
        }
        return key;
    }

    @Override
    public void delete(long id) {
        template.update(SQL_DELETE, id);
    }

    @Override
    public void delete(GiftCertificate entity) {
        delete(entity.getId());
    }

    @Override
    public List<GiftCertificate> retrieveByCriteria(Criteria criteria) {
        SqlCriteriaParser parser = new SqlCriteriaParser(criteria);
        List<GiftCertificate> certs
                = template.query(parser.getQuery(), mapper, parser.getParams());
        for(GiftCertificate certificate : certs){
            certificate.setTags(retrieveTags(certificate.getId()));
        }
        return certs;
    }

    @Override
    public void update(GiftCertificate entity) {
        CertificateUpdater updater = new CertificateUpdater(entity);
        updater.updateCertificate();
    }

    @Override
    public List<Tag> retrieveTags(long id){
        return template.query(SQL_SELECT_TAGS, new TagRowMapper(), id);
    }

    private static class SqlCriteriaParser {

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

        private final Criteria criteria;
        private List<Object> params = new ArrayList<>();
        private StringBuilder sqlBuilder;
        private StringBuilder orderBuilder = new StringBuilder();
        private StringBuilder conditionBuilder = new StringBuilder();

        public SqlCriteriaParser(Criteria criteria){
            this.criteria = criteria;
            sqlBuilder = new StringBuilder();
        }

        private String getQuery(){
            String tagName = criteria.get(Criteria.ParamName.RETRIEVE_BY_TAG_NAME);

            parseOrder();
            parsePartNameDescConditions();
            parseTagName();

            sqlBuilder.append(conditionBuilder);

            if (tagName != null){
                sqlBuilder.append(GROUP_BY);
            }
            sqlBuilder.append(orderBuilder);
            return sqlBuilder.toString();
        }

        private Object[] getParams(){
            return params.toArray();
        }

        private void parseTagName(){
            String tagName = criteria.get(Criteria.ParamName.RETRIEVE_BY_TAG_NAME);

            if(tagName != null){
                sqlBuilder.append(SELECT_BY_TAG);
                if (params.isEmpty()){
                    conditionBuilder.append(" WHERE ");
                } else {
                    conditionBuilder.append(" AND ");
                }
                conditionBuilder
                        .append(" ")
                        .append(DBMetadata.TAG_TABLE_NAME)
                        .append(" LIKE ")
                        .append("?");
                params.add(tagName);
            } else {
                sqlBuilder.append(SQL_SELECT);
            }

        }

        private void parsePartNameDescConditions(){

            String namePart = criteria.get(Criteria.ParamName.RETRIEVE_BY_PART_OF_NAME);
            String descPart = criteria.get(Criteria.ParamName.RETRIEVE_BY_PART_OF_DESCRIPTION);

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
                namePart = "%"+namePart+"%";
                descPart = "%"+descPart+"%";

                conditionBuilder
                        .append(" WHERE ")
                        .append(DBMetadata.CERTIFICATES_TABLE_NAME)
                        .append(" LIKE ")
                        .append("?")
                        .append(conjunction)
                        .append(DBMetadata.CERTIFICATES_TABLE_DESC)
                        .append(" LIKE ")
                        .append("?");
                params.add(namePart);
                params.add(descPart);
            }
        }

        private void parseOrder(){
            boolean isFull = false;
            if (criteria.containsKey(Criteria.ParamName.ORDER_BY_DATE_ASC)){
                orderBuilder
                        .append(" ORDER BY ")
                        .append(DBMetadata.CERTIFICATES_TABLE_CREATED)
                        .append(" ASC");
                isFull = true;
            }
            if (criteria.containsKey(Criteria.ParamName.ORDER_BY_DATE_DESC)){
                orderBuilder
                        .append(" ORDER BY ")
                        .append(DBMetadata.CERTIFICATES_TABLE_CREATED)
                        .append(" DESC");
                isFull = true;
            }

            if (criteria.containsKey(Criteria.ParamName.ORDER_BY_NAME_ASC)){
                orderBuilder
                        .append(isFull ? ", " : " ORDER BY ")
                        .append(DBMetadata.CERTIFICATES_TABLE_NAME)
                        .append(" ASC");
                isFull = true;
            }
            if (criteria.containsKey(Criteria.ParamName.ORDER_BY_NAME_DESC)){
                orderBuilder
                        .append(isFull ? ", " : " ORDER BY ")
                        .append(DBMetadata.CERTIFICATES_TABLE_NAME)
                        .append(" DESC");
            }
        }

    }

    private class CertificateUpdater{

        private StringBuilder updateColumnsQuery = new StringBuilder();
        private List<Object> params = new ArrayList<>();
        private GiftCertificate entity;

        CertificateUpdater(GiftCertificate entity){
            this.entity = entity;
        }

        void updateCertificate(){
            Optional<GiftCertificate> oldCertificate = retrieveById(entity.getId());

            if(oldCertificate.isEmpty()){
                return;
            }

            Map<String,Object> toUpdate = getMapOfParamsToUpdate(oldCertificate.get());
            if(!toUpdate.isEmpty()){
                buildConditionsAndParams(toUpdate);
                params.add(entity.getId());
                template.update(
                        (String.format("UPDATE %s SET ", DBMetadata.CERTIFICATES_TABLE) +
                        updateColumnsQuery +
                        String.format("WHERE %s=?", DBMetadata.CERTIFICATES_TABLE_ID)),
                        params.toArray());
                updateTags(oldCertificate.get());
            }
        }

        private void updateTags(GiftCertificate oldCertificate){
            if(entity.getTags() == null){
                return;
            }
            HashSet<Tag> oldTags = new HashSet<>(oldCertificate.getTags());
            HashSet<Tag> newTags = new HashSet<>(entity.getTags());

            HashSet<Tag> temp = new HashSet<>(oldTags);

            temp.retainAll(newTags);
            oldTags.removeAll(temp);
            newTags.removeAll(temp);

            for(Tag tag : oldTags){
                template.update(SQL_REMOVE_TAG_LINK, entity.getId(),tag.getId());
            }
            for(Tag tag : newTags){
                template.update(SQL_CREATE_TAG_LINK, entity.getId(),tag.getId());
            }
        }

        private void buildConditionsAndParams(Map<String,Object> paramsToUpdate){
            int size = paramsToUpdate.entrySet().size();
            String newDesc = (String) paramsToUpdate.get(DBMetadata.CERTIFICATES_TABLE_DESC);
            if(newDesc != null
                    && (newDesc.equalsIgnoreCase("null") || newDesc.isBlank())){
                paramsToUpdate.replace(DBMetadata.CERTIFICATES_TABLE_DESC, null);
            }
            for(Map.Entry<String,Object> parameter : paramsToUpdate.entrySet()){
                updateColumnsQuery
                        .append(" ")
                        .append(parameter.getKey())
                        .append(" =?")
                        .append(size>1 ? ", " : " ");
                params.add(parameter.getValue());
                size--;
            }
        }

        private Map<String,Object> getMapOfParamsToUpdate(GiftCertificate oldCert){
            Map<String,Object> oldParams = getMapOfFields(oldCert);
            Map<String,Object> newParams = getMapOfFields(entity);

            newParams.entrySet().removeAll(oldParams.entrySet());
            newParams.entrySet().removeIf(e-> e.getValue() == null);
            return newParams;
        }

        private Map<String, Object> getMapOfFields(GiftCertificate certificate){
            Map<String, Object> res = new HashMap<>();
            res.put(DBMetadata.CERTIFICATES_TABLE_NAME, certificate.getName());
            res.put(DBMetadata.CERTIFICATES_TABLE_DESC, certificate.getDescription());
            res.put(DBMetadata.CERTIFICATES_TABLE_PRICE, certificate.getPrice());
            res.put(DBMetadata.CERTIFICATES_TABLE_DURATION, certificate.getDuration());
            res.put(DBMetadata.CERTIFICATES_TABLE_CREATED, certificate.getCreateDate() == null ? null : Timestamp.valueOf(certificate.getCreateDate()));
            res.put(DBMetadata.CERTIFICATES_TABLE_LAST_UPDATE, certificate.getLastUpdateDate() == null ? null : Timestamp.valueOf(certificate.getLastUpdateDate()));
            return res;
        }


    }
}
