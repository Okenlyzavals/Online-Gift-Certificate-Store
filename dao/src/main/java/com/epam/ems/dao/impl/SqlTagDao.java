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
    private static final String SQL_INSERT = String.format(
            "INSERT INTO %s (%s,%s) VALUES (DEFAULT,?)",
            DBMetadata.TAG_TABLE, DBMetadata.TAG_TABLE_ID,
            DBMetadata.TAG_TABLE_NAME);
    private static final String SQL_DELETE = String.format(
            "DELETE FROM %s WHERE %s=?",
            DBMetadata.TAG_TABLE, DBMetadata.TAG_TABLE_ID);


    private final JdbcTemplate template;
    private TagRowMapper tagRowMapper;

    @Autowired
    public SqlTagDao(DataSource dataSource, TagRowMapper rowMapper) {
        template = new JdbcTemplate(dataSource);
        this.tagRowMapper = rowMapper;
    }

    @Override
    public Optional<Tag> retrieveById(long id) {
        return template.query(
                SQL_SELECT_ID,
                tagRowMapper,
                id).stream().findAny();
    }

    @Override
    public List<Tag> retrieveAll() {
        return template.query(SQL_SELECT, tagRowMapper);
    }

    @Override
    public Long create(Tag entity) {
        KeyHolder holder = new GeneratedKeyHolder();
        template.update(con -> {
            PreparedStatement statement = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, entity.getName());
            return statement;
        },holder);
        return holder.getKey().longValue();
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
}
