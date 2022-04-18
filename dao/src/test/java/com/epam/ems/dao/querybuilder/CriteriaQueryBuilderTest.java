package com.epam.ems.dao.querybuilder;

import com.epam.ems.dao.constant.DBMetadata;
import com.epam.ems.dao.entity.criteria.Criteria;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CriteriaQueryBuilderTest {

    @Test
    void testBuildEmptyQuery(){
        String expected = "SELECT * FROM "+ DBMetadata.CERTIFICATES_TABLE;

        CriteriaQueryBuilder builder = new CriteriaQueryBuilder();
        builder.parse(new Criteria());
        String actual = builder.getQuery();
        assertEquals(expected, actual);
    }

    @Test
    void testAddNullToQuery(){
        String expected = "SELECT * FROM "+ DBMetadata.CERTIFICATES_TABLE;
        Criteria criteria = new Criteria();
        criteria.put(null,"");

        CriteriaQueryBuilder builder = new CriteriaQueryBuilder();
        builder.parse(criteria);
        String actual = builder.getQuery();
        assertEquals(expected, actual);
    }

    @Test
    void testBuildQueryByTagName(){
        String expectedQuery = String.format(
                "SELECT * FROM %s " +
                        "RIGHT JOIN %s ON %s = %s " +
                        "LEFT JOIN %s ON %s=%s WHERE %s LIKE ? GROUP BY %s",
                DBMetadata.CERTIFICATES_TABLE, DBMetadata.CERT_HAS_TAG_TABLE,
                DBMetadata.CERTIFICATES_TABLE_ID, DBMetadata.CERT_HAS_TAG_TABLE_ID_CERT,
                DBMetadata.TAG_TABLE, DBMetadata.TAG_TABLE_ID,
                DBMetadata.CERT_HAS_TAG_TABLE_ID_TAG,DBMetadata.TAG_TABLE_NAME,
                DBMetadata.CERTIFICATES_TABLE_ID);
        Object[] expectedParams = {"testing"};
        CriteriaQueryBuilder builder = new CriteriaQueryBuilder();
        Criteria criteria = new Criteria();
        criteria.put(Criteria.ParamName.TAG_NAME, "testing");
        builder.parse(criteria);

        String actualQuery = builder.getQuery();
        Object[] actualParams = builder.getParams();

        assertArrayEquals(expectedParams, actualParams);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void testBuildQueryByNameAndDesc(){
        String expectedQuery = String.format("SELECT * FROM %s WHERE ((%s LIKE ? OR %s IS NULL) OR (%s LIKE ?))",
                DBMetadata.CERTIFICATES_TABLE, DBMetadata.CERTIFICATES_TABLE_NAME, DBMetadata.CERTIFICATES_TABLE_NAME,
                DBMetadata.CERTIFICATES_TABLE_DESC);
        Object[] expectedParams = {"%%","%test%"};
        Criteria criteria = new Criteria();
        criteria.put(Criteria.ParamName.NAME_CONTAINS, "");
        criteria.put(Criteria.ParamName.DESCRIPTION_CONTAINS, "test");

        CriteriaQueryBuilder builder = new CriteriaQueryBuilder();
        builder.parse(criteria);
        String actualQuery = builder.getQuery();
        Object[] actualParams = builder.getParams();

        assertArrayEquals(expectedParams, actualParams);
        assertEquals(expectedQuery, actualQuery);

    }

    @Test
    void testBuildQueryByTagNameAndNameAndDesc(){
        String expectedQuery = String.format(
                "SELECT * FROM %s " +
                        "RIGHT JOIN %s ON %s = %s " +
                        "LEFT JOIN %s ON %s=%s WHERE ((%s LIKE ?) " +
                        "OR (%s LIKE ?)) AND %s LIKE ? GROUP BY %s",
                DBMetadata.CERTIFICATES_TABLE, DBMetadata.CERT_HAS_TAG_TABLE,
                DBMetadata.CERTIFICATES_TABLE_ID, DBMetadata.CERT_HAS_TAG_TABLE_ID_CERT,
                DBMetadata.TAG_TABLE, DBMetadata.TAG_TABLE_ID,
                DBMetadata.CERT_HAS_TAG_TABLE_ID_TAG,
                DBMetadata.CERTIFICATES_TABLE_NAME, DBMetadata.CERTIFICATES_TABLE_DESC,
                DBMetadata.TAG_TABLE_NAME, DBMetadata.CERTIFICATES_TABLE_ID);
        Object[] expectedParams = {"%testName%","%testDesc%","testTag"};
        Criteria criteria = new Criteria();
        criteria.put(Criteria.ParamName.NAME_CONTAINS, "testName");
        criteria.put(Criteria.ParamName.DESCRIPTION_CONTAINS, "testDesc");
        criteria.put(Criteria.ParamName.TAG_NAME, "testTag");

        CriteriaQueryBuilder builder = new CriteriaQueryBuilder();
        builder.parse(criteria);
        String actualQuery = builder.getQuery();
        Object[] actualParams = builder.getParams();

        assertArrayEquals(expectedParams, actualParams);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void testBuildQueryWithOrder(){
        String expectedQuery = String.format("SELECT * FROM %s ORDER BY %s ASC",
                DBMetadata.CERTIFICATES_TABLE, DBMetadata.CERTIFICATES_TABLE_CREATED);
        Criteria criteria = new Criteria();
        criteria.put(Criteria.ParamName.ORDER_DATE_ASC, "");

        CriteriaQueryBuilder builder = new CriteriaQueryBuilder();
        builder.parse(criteria);
        String actualQuery = builder.getQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void testBuildQueryWithConflictingOrder(){
        String expectedQuery = String.format("SELECT * FROM %s ORDER BY %s DESC, %s ASC",
                DBMetadata.CERTIFICATES_TABLE, DBMetadata.CERTIFICATES_TABLE_CREATED,
                DBMetadata.CERTIFICATES_TABLE_NAME);
        Criteria criteria = new Criteria();
        criteria.put(Criteria.ParamName.ORDER_DATE_ASC, "");
        criteria.put(Criteria.ParamName.ORDER_DATE_DESC, "");
        criteria.put(Criteria.ParamName.ORDER_NAME_DESC, "");
        criteria.put(Criteria.ParamName.ORDER_NAME_ASC, "");

        CriteriaQueryBuilder builder = new CriteriaQueryBuilder();
        builder.parse(criteria);
        String actualQuery = builder.getQuery();

        assertEquals(expectedQuery, actualQuery);
    }
}
