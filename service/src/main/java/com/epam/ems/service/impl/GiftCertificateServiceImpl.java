package com.epam.ems.service.impl;

import com.epam.ems.dao.GiftCertificateDao;
import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.dao.entity.criteria.Criteria;
import com.epam.ems.service.GiftCertificateService;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
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
    public List<GiftCertificateDto> getAll(int page, int elements) {
        return dao.retrieveAll(page,elements)
                .stream()
                .map(e->mapper.map(e))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GiftCertificateDto insert(GiftCertificateDto entity){
        entity.setLastUpdateDate(LocalDateTime.now());

        GiftCertificate toCreate = mapper.extract(entity);
        toCreate.setTags(getTagsPreparedForDbOperations(toCreate));

        return mapper.map(dao.create(toCreate));
    }

    @Override
    @Transactional
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
    public List<GiftCertificateDto> getByCriteria(Map<String,Object> criteria, int page, int elements) {
        return dao.retrieveByCriteria(mapToCriteria(criteria),page,elements)
                .stream()
                .map(e->mapper.map(e))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GiftCertificateDto update(GiftCertificateDto entity) throws NoSuchEntityException {

        GiftCertificate oldCert = dao.retrieveById(entity.getId())
                .orElseThrow(()->new NoSuchEntityException(GiftCertificate.class));

        entity.setLastUpdateDate(LocalDateTime.now());
        GiftCertificate toUpdate = getUpdatedCertificate(oldCert, mapper.extract(entity));
        toUpdate.setTags(getTagsPreparedForDbOperations(toUpdate));

        return mapper.map(dao.update(toUpdate));
    }

    private Set<Tag> getTagsPreparedForDbOperations(GiftCertificate certificate){
        Set<Tag> currentTags = certificate.getTags();
        if(!CollectionUtils.isEmpty(currentTags)){
            Set<Tag> preparedTags = new HashSet<>();

            for(Tag tag : currentTags){
                Optional<Tag> tagFromDb = tagDao.findByName(tag.getName()).or(()-> {
                    if(tag.getId() == null){
                        return Optional.empty();
                    }
                    return tagDao.retrieveById(tag.getId());
                });
                tagFromDb.ifPresentOrElse(
                        preparedTags::add,
                        ()->preparedTags.add(Tag.builder().name(tag.getName()).build()));
            }
            return preparedTags;
        }
        return currentTags;
    }

    private Criteria mapToCriteria(Map<String,Object> map){

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

    private GiftCertificate getUpdatedCertificate(GiftCertificate oldCert, GiftCertificate updatedCert){
        GiftCertificate result = oldCert.toBuilder().build();

        try {
            for (Field field : updatedCert.getClass().getDeclaredFields()){
                field.setAccessible(true);
                if(field.get(updatedCert) != null && !field.get(updatedCert).equals(field.get(oldCert))){
                    field.set(result, field.get(updatedCert));
                }
            }
        } catch (IllegalAccessException e) {
            result = oldCert;
        }
        return result;
    }
}
