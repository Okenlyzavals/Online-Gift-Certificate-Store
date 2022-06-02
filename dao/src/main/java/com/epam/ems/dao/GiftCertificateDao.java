package com.epam.ems.dao;

import com.epam.ems.dao.custom.GiftCertificateDaoCustom;
import com.epam.ems.dao.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftCertificateDao extends JpaRepository<GiftCertificate,Long>, GiftCertificateDaoCustom {

}
