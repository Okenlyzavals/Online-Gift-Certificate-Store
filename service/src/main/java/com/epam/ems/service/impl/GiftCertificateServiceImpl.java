package com.epam.ems.service.impl;

import com.epam.ems.dao.GiftCertificateDao;
import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.dao.entity.criteria.CertificateCriteria;
import com.epam.ems.service.GiftCertificateService;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.exception.UpdateException;
import com.epam.ems.service.mapper.Mapper;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao dao;
    private final TagDao tagDao;
    private final Mapper<GiftCertificate, GiftCertificateDto> mapper;

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
                dao.findById(id)
                .orElseThrow(()->new NoSuchEntityException(GiftCertificate.class)));
    }

    @Override
    public Page<GiftCertificateDto> getAll(int page, int elements) {
        Pageable request = PageRequest.of(page,elements);
        Page<GiftCertificate> result =  dao.findAll(request);
        return new PageImpl<>(
                result.stream().map(mapper::map).collect(Collectors.toList()),
                request,
                result.getTotalElements());
    }

    @Override
    public GiftCertificateDto insert(GiftCertificateDto entity){
        entity.setLastUpdateDate(LocalDateTime.now());
        entity.setId(null);

        GiftCertificate toCreate = mapper.extract(entity);
        toCreate.setTags(getTagsPreparedForDbOperations(toCreate));

        return mapper.map(dao.save(toCreate));
    }

    @Override
    public void delete(Long id) throws NoSuchEntityException {
        dao.findById(id).ifPresentOrElse(
                dao::delete,
                ()-> {throw new NoSuchEntityException(GiftCertificate.class);});
    }

    @Override
    public void delete(GiftCertificateDto entity) throws NoSuchEntityException {
        delete(entity.getId());
    }

    @Override
    public Page<GiftCertificateDto> getByCriteria(Map<String,Object> criteria, int page, int elements) {

        Pageable request = PageRequest.of(page,elements);
        Page<GiftCertificate> result =  dao.retrieveByCriteria(mapToCriteria(criteria), request);
        return new PageImpl<>(
                result.stream().map(mapper::map).collect(Collectors.toList()),
                request,
                result.getTotalElements());
    }

    @Override
    public GiftCertificateDto update(GiftCertificateDto entity) throws NoSuchEntityException {

        GiftCertificate oldCert = dao.findById(entity.getId())
                .orElseThrow(()->new NoSuchEntityException(GiftCertificate.class));

        entity.setLastUpdateDate(LocalDateTime.now());
        GiftCertificate toUpdate = getUpdatedCertificate(oldCert, mapper.extract(entity));
        toUpdate.setTags(getTagsPreparedForDbOperations(toUpdate));

        return mapper.map(dao.save(toUpdate));
    }

    private Set<Tag> getTagsPreparedForDbOperations(GiftCertificate certificate){
        Set<Tag> currentTags = certificate.getTags();
        if(!CollectionUtils.isEmpty(currentTags)){
            Set<Tag> preparedTags = new HashSet<>();

            for(Tag tag : currentTags){
                Optional<Tag> tagFromDb = tagDao.findDistinctByName(tag.getName()).or(()-> {
                    if(tag.getId() == null){
                        return Optional.empty();
                    }
                    return tagDao.findById(tag.getId());
                });
                tagFromDb.ifPresentOrElse(
                        preparedTags::add,
                        ()->preparedTags.add(Tag.builder().name(tag.getName()).build()));
            }
            return preparedTags;
        }
        return currentTags;
    }

    private CertificateCriteria mapToCriteria(Map<String,Object> map){

        List<String> paramNames = Arrays.stream(CertificateCriteria.ParamName.values())
                .map(Enum::name).collect(Collectors.toList());

        CertificateCriteria criteria = new CertificateCriteria();
        if(map == null){
            return criteria;
        }

        map.entrySet().stream()
                .filter(e->paramNames.contains(e.getKey()))
                .forEach(e-> criteria.put(CertificateCriteria.ParamName.valueOf(e.getKey()), e.getValue()));

        return criteria;
    }

    private GiftCertificate getUpdatedCertificate(GiftCertificate oldCert, GiftCertificate updatedCert){
        GiftCertificate result = oldCert.toBuilder().build();

        try{
            PropertyUtils.describe(updatedCert).entrySet().stream()
                    .filter(p -> p.getValue() != null)
                    .filter(p -> ! p.getKey().equals("class"))
                    .forEach(p -> {
                        try {
                            PropertyUtils.setProperty(result, p.getKey(), p.getValue());
                        } catch (Exception e) {
                            throw new UpdateException(GiftCertificate.class);
                        }
                    });
        } catch (Exception e){
            throw new UpdateException(GiftCertificate.class);
        }

        return result;
    }
}
