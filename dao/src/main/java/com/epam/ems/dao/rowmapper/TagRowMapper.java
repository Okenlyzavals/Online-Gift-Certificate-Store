package com.epam.ems.dao.rowmapper;

import com.epam.ems.dao.constant.DBMetadata;
import com.epam.ems.dao.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagRowMapper implements RowMapper<Tag> {
    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Tag.builder()
                .name(rs.getString(DBMetadata.TAG_TABLE_NAME))
                .id(rs.getLong(DBMetadata.TAG_TABLE_ID))
                .build();
    }
}
