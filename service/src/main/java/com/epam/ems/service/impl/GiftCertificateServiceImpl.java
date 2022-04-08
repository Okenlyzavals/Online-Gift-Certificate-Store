package com.epam.ems.service.impl;

import com.epam.ems.dao.GiftCertificateDao;
import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.model.criteria.Criteria;
import com.epam.ems.service.GiftCertificateService;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@ComponentScan({"com.epam.ems.dao", "com.epam.ems.service"})
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateDao dao;
    private TagDao tagDao;
    private Mapper<GiftCertificate, GiftCertificateDto> mapper;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao dao, TagDao tagDao,
                                      Mapper<GiftCertificate, GiftCertificateDto> mapper) {
        this.dao = dao;
        this.tagDao = tagDao;
        this.mapper = mapper;
    }

    @Override
    public GiftCertificateDto getById(Long id) {
        Optional<GiftCertificate> cert = dao.retrieveById(id);
        if(cert.isEmpty()){
            return null;
        }
        return mapper.map(cert.get());
    }

    @Override
    public List<GiftCertificateDto> getAll() {
        return dao.retrieveAll()
                .stream()
                .map(e->mapper.map(e))
                .collect(Collectors.toList());
    }

    @Override
    public void insert(GiftCertificateDto entity) {
        entity.setLastUpdateDate(LocalDateTime.now());
        GiftCertificate certificate = mapper.extract(entity);
        List<Tag> newTags = certificate.getTags()
                .stream()
                .filter(e-> e.getId() == null || e.getId() == 0)
                .collect(Collectors.toList());
        for(Tag tag : newTags){
            tag.setId(tagDao.create(tag));
        }
        dao.create(certificate);
    }

    @Override
    public void delete(Long id) {
        dao.delete(id);
    }

    @Override
    public void delete(GiftCertificateDto entity) {
        dao.delete(mapper.extract(entity));
    }

    @Override
    public List<GiftCertificateDto> getByCriteria(Criteria criteria) {
        return dao.retrieveByCriteria(criteria)
                .stream()
                .map(e->mapper.map(e))
                .collect(Collectors.toList());
    }

    @Override
    public void update(GiftCertificateDto entity) {
        entity.setLastUpdateDate(LocalDateTime.now());
        dao.update(mapper.extract(entity));
    }
}
