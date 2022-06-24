package com.epam.ems.dao.criteria.parser;

import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.criteria.CertificateCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

public interface CertificateCriteriaQueryParameterParser<T> {

    T[] parse(CertificateCriteria criteria, CriteriaBuilder builder, Root<GiftCertificate> root);
}
