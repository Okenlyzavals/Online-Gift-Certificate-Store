package com.epam.ems.dao.criteria;

import com.epam.ems.dao.entity.criteria.CertificateCriteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
class CriteriaTest {

    @Test
    void testPutNullParameter(){
        CertificateCriteria criteria = new CertificateCriteria();

        Object result = criteria.put(null,"");

        assertNull(result);
    }

    @Test
    void testDateCriteriaReplaceAscendingWithDescending(){
        CertificateCriteria criteria = new CertificateCriteria();

        criteria.put(CertificateCriteria.ParamName.ORDER_DATE_ASC,"");
        criteria.put(CertificateCriteria.ParamName.ORDER_DATE_DESC,"");

        assertNull(criteria.get(CertificateCriteria.ParamName.ORDER_DATE_ASC));
        assertNotNull(criteria.get(CertificateCriteria.ParamName.ORDER_DATE_DESC));
    }

    @Test
    void testDateCriteriaReplaceDescendingWithAscending(){
        CertificateCriteria criteria = new CertificateCriteria();

        criteria.put(CertificateCriteria.ParamName.ORDER_DATE_DESC,"");
        criteria.put(CertificateCriteria.ParamName.ORDER_DATE_ASC,"");

        assertNull(criteria.get(CertificateCriteria.ParamName.ORDER_DATE_DESC));
        assertNotNull(criteria.get(CertificateCriteria.ParamName.ORDER_DATE_ASC));
    }

    @Test
    void testNameCriteriaReplaceAscendingWithDescending(){
        CertificateCriteria criteria = new CertificateCriteria();

        criteria.put(CertificateCriteria.ParamName.ORDER_NAME_ASC,"");
        criteria.put(CertificateCriteria.ParamName.ORDER_NAME_DESC,"");

        assertNull(criteria.get(CertificateCriteria.ParamName.ORDER_NAME_ASC));
        assertNotNull(criteria.get(CertificateCriteria.ParamName.ORDER_NAME_DESC));
    }

    @Test
    void testNameCriteriaReplaceDescendingWithAscending(){
        CertificateCriteria criteria = new CertificateCriteria();

        criteria.put(CertificateCriteria.ParamName.ORDER_NAME_DESC,"");
        criteria.put(CertificateCriteria.ParamName.ORDER_NAME_ASC,"");

        assertNull(criteria.get(CertificateCriteria.ParamName.ORDER_NAME_DESC));
        assertNotNull(criteria.get(CertificateCriteria.ParamName.ORDER_NAME_ASC));
    }

}
