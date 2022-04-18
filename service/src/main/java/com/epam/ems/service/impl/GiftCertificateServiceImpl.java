package com.epam.ems.service.impl;

import com.epam.ems.dao.GiftCertificateDao;
import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.criteria.Criteria;
import com.epam.ems.service.GiftCertificateService;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.exception.DuplicateEntityException;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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
    public GiftCertificateDto getById(Long id) throws NoSuchEntityException {
        return mapper.map(
                dao.retrieveById(id)
                .orElseThrow(()->new NoSuchEntityException(GiftCertificate.class)));
    }

    @Override
    public List<GiftCertificateDto> getAll() {
        return dao.retrieveAll()
                .stream()
                .map(e->mapper.map(e))
                .collect(Collectors.toList());
    }

    @Override
    public void insert(GiftCertificateDto entity){
        entity.setLastUpdateDate(LocalDateTime.now());
        GiftCertificate certificate = mapper.extract(entity);
        dao.create(certificate);
    }

    @Override
    public void delete(Long id) throws NoSuchEntityException {
        if(dao.retrieveById(id).isEmpty()){
            throw new NoSuchEntityException(GiftCertificate.class);
        }
        dao.delete(id);
    }

    @Override
    public void delete(GiftCertificateDto entity) throws NoSuchEntityException {
        delete(entity.getId());
    }

    @Override
    public List<GiftCertificateDto> getByCriteria(Map<String,String> criteria) {
        return dao.retrieveByCriteria(mapToCriteria(criteria))
                .stream()
                .map(e->mapper.map(e))
                .collect(Collectors.toList());
    }

    @Override
    public void update(GiftCertificateDto entity) throws NoSuchEntityException {
        dao.retrieveById(entity.getId())
                .orElseThrow(()->new NoSuchEntityException(GiftCertificate.class));

        entity.setLastUpdateDate(LocalDateTime.now());
        dao.update(mapper.extract(entity));
    }

    private Criteria mapToCriteria(Map<String,String> map){

        List<String> paramNames = Arrays.stream(Criteria.ParamName.values())
                .map(Enum::name).collect(Collectors.toList());

        Criteria criteria = new Criteria();
        if(map == null){
            return criteria;
        }

        map.entrySet().stream()
                .filter(e->paramNames.contains(e.getKey()))
                .forEach(e-> criteria.put(Criteria.ParamName.valueOf(e.getKey()), e.getValue()));

        return criteria;
    }
}
