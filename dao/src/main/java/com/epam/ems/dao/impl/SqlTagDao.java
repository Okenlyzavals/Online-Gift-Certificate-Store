package com.epam.ems.dao.impl;

import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.constant.DBMetadata;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.dao.rowmapper.TagRowMapper;
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
import java.util.List;
import java.util.Optional;

@Repository
@ComponentScan("com.epam.ems.dao")
public class SqlTagDao implements TagDao {

    private static final String SQL_SELECT=String.format(
            "SELECT * FROM %s", DBMetadata.TAG_TABLE);
    private static final String SQL_SELECT_ID=
            SQL_SELECT + String.format(" WHERE %s=?", DBMetadata.TAG_TABLE_ID);
    private static final String SQL_SELECT_NAME=
            SQL_SELECT + String.format(" WHERE %s=?", DBMetadata.TAG_TABLE_NAME);
    private static final String SQL_SELECT_BY_CERTIFICATE = String.format(
            "SELECT * FROM %s " +
                    "LEFT JOIN %s " +
                    "ON %s=%s WHERE %s=?",
            DBMetadata.CERT_HAS_TAG_TABLE, DBMetadata.TAG_TABLE,
            DBMetadata.TAG_TABLE_ID, DBMetadata.CERT_HAS_TAG_TABLE_ID_TAG,
            DBMetadata.CERT_HAS_TAG_TABLE_ID_CERT);
    private static final String SQL_INSERT = String.format(
            "INSERT INTO %s (%s,%s) VALUES (DEFAULT,?)",
            DBMetadata.TAG_TABLE, DBMetadata.TAG_TABLE_ID,
            DBMetadata.TAG_TABLE_NAME);
    private static final String SQL_DELETE = String.format(
            "DELETE FROM %s WHERE %s=?",
            DBMetadata.TAG_TABLE, DBMetadata.TAG_TABLE_ID);


    private final JdbcTemplate template;
    private final TagRowMapper tagRowMapper;

    @Autowired
    public SqlTagDao(DataSource dataSource, TagRowMapper rowMapper) {
        template = new JdbcTemplate(dataSource);
        this.tagRowMapper = rowMapper;
    }

    @Override
    public Optional<Tag> retrieveById(long id) {
        List<Tag> tags = template.query(SQL_SELECT_ID, tagRowMapper, id);
        return !tags.isEmpty() ? Optional.of(tags.get(0)) : Optional.empty();
    }

    @Override
    public List<Tag> retrieveAll() {
        return template.query(SQL_SELECT, tagRowMapper);
    }

    @Override
    public Tag create(Tag entity) {
        KeyHolder holder = new GeneratedKeyHolder();
        template.update(con -> {
            PreparedStatement statement = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, entity.getName());
            return statement;
        },holder);
        Long key = holder.getKey().longValue();
        return retrieveById(key).get();
    }

    @Override
    public void delete(long id) {
        template.update(SQL_DELETE, id);
    }

    @Override
    public void delete(Tag entity) {
        template.update(SQL_DELETE, entity.getId());
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return template.query(SQL_SELECT_NAME, tagRowMapper, name).stream().findAny();
    }

    @Override
    public List<Tag> retrieveTagsByCertificateId(Long certificateId) {
        return template.query(SQL_SELECT_BY_CERTIFICATE, tagRowMapper, certificateId);
    }
}
