package com.epam.ems.dao;

import com.epam.ems.dao.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao extends AbstractDao<Tag>{

    Optional<Tag> findByName(String name);

    List<Tag> retrieveTagsByCertificateId(Long certificateId);
}
