package com.epam.ems.dao.impl;

import com.epam.ems.dao.GiftCertificateDao;
import com.epam.ems.dao.constant.DBMetadata;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.dao.querybuilder.CriteriaQueryBuilder;
import com.epam.ems.dao.querybuilder.UpdateQueryBuilder;
import com.epam.ems.dao.rowmapper.CertificateRowMapper;
import com.epam.ems.dao.rowmapper.TagRowMapper;
import com.epam.ems.dao.entity.criteria.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

@Repository
@ComponentScan({"com.epam.ems.dao"})
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


    private final JdbcTemplate template;
    private final CriteriaQueryBuilder criteriaQueryBuilder;
    private final UpdateQueryBuilder updateQueryBuilder;
    private CertificateRowMapper mapper;

    @Autowired
    public SqlGiftCertificateDao(DataSource dataSource,
                                 CertificateRowMapper mapper,
                                 CriteriaQueryBuilder criteriaQueryBuilder,
                                 UpdateQueryBuilder updateQueryBuilder){
        template = new JdbcTemplate(dataSource);
        this.criteriaQueryBuilder = criteriaQueryBuilder;
        this.updateQueryBuilder = updateQueryBuilder;
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
        criteriaQueryBuilder.parse(criteria);
        List<GiftCertificate> certs
                = template.query(
                        criteriaQueryBuilder.getQuery(),
                        mapper,
                        criteriaQueryBuilder.getParams());
        for(GiftCertificate certificate : certs){
            certificate.setTags(retrieveTags(certificate.getId()));
        }
        return certs;
    }

    @Override
    public void update(GiftCertificate entity) {

        GiftCertificate oldCertificate = retrieveById(entity.getId()).get();
        MultiValueMap<String, List<Object>> queries = updateQueryBuilder.parse(entity, oldCertificate);
        for(String key : queries.keySet()){
            for(List<Object> params : queries.get(key)){
                template.update(key, params.toArray());
            }
        }
    }

    @Override
    public List<Tag> retrieveTags(long id){
        return template.query(SQL_SELECT_TAGS, new TagRowMapper(), id);
    }

}
