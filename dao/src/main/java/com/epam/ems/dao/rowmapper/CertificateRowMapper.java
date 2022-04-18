package com.epam.ems.dao.rowmapper;

import com.epam.ems.dao.constant.DBMetadata;
import com.epam.ems.dao.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class CertificateRowMapper implements RowMapper<GiftCertificate> {


    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {

        return GiftCertificate.builder()
                .id(rs.getLong(DBMetadata.CERTIFICATES_TABLE_ID))
                .name(rs.getString(DBMetadata.CERTIFICATES_TABLE_NAME))
                .description(rs.getString(DBMetadata.CERTIFICATES_TABLE_DESC))
                .price(rs.getBigDecimal(DBMetadata.CERTIFICATES_TABLE_PRICE))
                .duration(rs.getInt(DBMetadata.CERTIFICATES_TABLE_DURATION))
                .createDate(rs.getTimestamp(DBMetadata.CERTIFICATES_TABLE_CREATED).toLocalDateTime())
                .lastUpdateDate(rs.getTimestamp(DBMetadata.CERTIFICATES_TABLE_LAST_UPDATE).toLocalDateTime())
                .tags(new ArrayList<>())
                .build();
    }

}
