package com.epam.ems.dao.querybuilder;

import com.epam.ems.dao.constant.DBMetadata;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateQueryBuilderTest {

    private final UpdateQueryBuilder builder;
    private final GiftCertificate oldCertificate;

    {
        builder= new UpdateQueryBuilder();
        oldCertificate = GiftCertificate.builder()
                .id(5L)
                .name("Testing certificate")
                .description(null)
                .duration(14)
                .price(new BigDecimal("150.00000"))
                .createDate(LocalDateTime.ofEpochSecond(894370375L,0, ZoneOffset.UTC))
                .lastUpdateDate(LocalDateTime.ofEpochSecond(1484370375L,0, ZoneOffset.UTC))
                .tags(List.of(new Tag(1L,"Software testing"), new Tag(2L, "Epam")))
                .build();
    }

    @Test
    void testEmptyUpdateQuery(){
        GiftCertificate newCertificate = GiftCertificate.builder().build();
        MultiValueMap<String, List<Object>> expected = new LinkedMultiValueMap<>();

        MultiValueMap<String, List<Object>> actual = builder.parse(newCertificate, oldCertificate);

        assertEquals(expected,actual);
    }

    @Test
    void testEmptyUpdateQueryWithNullParams(){
        MultiValueMap<String, List<Object>> expected = new LinkedMultiValueMap<>();

        MultiValueMap<String, List<Object>> actual = builder.parse(null, null);

        assertEquals(expected,actual);
    }

    @Test
    void testTagListUpdateQuery(){
        MultiValueMap<String, List<Object>> expected = new LinkedMultiValueMap<>();
        GiftCertificate toUpdate = GiftCertificate
                .builder()
                .tags(List.of(new Tag(2L, "Epam"), new Tag(3L, "Software")))
                .build();
        expected.add(
                String.format("DELETE FROM %s WHERE %s=? AND %s=?",
                        DBMetadata.CERT_HAS_TAG_TABLE, DBMetadata.CERT_HAS_TAG_TABLE_ID_CERT,
                        DBMetadata.CERT_HAS_TAG_TABLE_ID_TAG)
                ,List.of(oldCertificate.getId(),1L));
        expected.add(
                String.format("INSERT INTO %s (%s,%s) VAlUES(?,?)",
                        DBMetadata.CERT_HAS_TAG_TABLE, DBMetadata.CERT_HAS_TAG_TABLE_ID_CERT,
                        DBMetadata.CERT_HAS_TAG_TABLE_ID_TAG)
                ,List.of(oldCertificate.getId(),3L));

        MultiValueMap<String, List<Object>> actual = builder.parse(toUpdate, oldCertificate);

        assertEquals(expected, actual);

    }

    @Test
    void testUpdateSetDescEmpty(){
        GiftCertificate newCertificate = GiftCertificate.builder().description("     ").build();
        MultiValueMap<String, List<Object>> expected = new LinkedMultiValueMap<>();
        expected.add(
                String.format(
                        "UPDATE %s SET %s=? WHERE %s=?",
                        DBMetadata.CERTIFICATES_TABLE, DBMetadata.CERTIFICATES_TABLE_DESC,
                        DBMetadata.CERTIFICATES_TABLE_ID)
                , Arrays.asList(null, 5L));

        MultiValueMap<String, List<Object>> actual = builder.parse(newCertificate, oldCertificate);

        assertEquals(expected,actual);
    }
}
