package com.epam.ems.dao;

import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.model.criteria.Criteria;

import java.util.List;

public interface GiftCertificateDao extends AbstractDao<GiftCertificate> {

    List<GiftCertificate> retrieveByCriteria(Criteria criteria);

    void update(GiftCertificate entity);

    List<Tag> retrieveTags(long id);
}
