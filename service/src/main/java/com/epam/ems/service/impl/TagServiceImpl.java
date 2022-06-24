package com.epam.ems.service.impl;

import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.service.TagService;
import com.epam.ems.service.dto.TagDto;
import com.epam.ems.service.exception.DuplicateEntityException;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao dao;
    private final Mapper<Tag, TagDto> mapper;

    @Autowired
    public TagServiceImpl(TagDao dao,
                          Mapper<Tag, TagDto> mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public TagDto getById(Long id) throws NoSuchEntityException {
        return mapper.map(dao.findById(id).orElseThrow(() -> new NoSuchEntityException(Tag.class)));
    }

    @Override
    public Page<TagDto> getAll(int page, int elements) {
        Pageable request = PageRequest.of(page,elements);
        Page<Tag> result =  dao.findAll(request);
        return new PageImpl<>(
                result.stream().map(mapper::map).collect(Collectors.toList()),
                request,
                result.getTotalElements());
    }

    @Override
    @Transactional
    public TagDto insert(TagDto entity) throws DuplicateEntityException {
        entity.setId(null);
        findDuplicate(entity).ifPresent(d->{
            throw new DuplicateEntityException(d.getId(), Tag.class);
        });
        return mapper.map(dao.save(mapper.extract(entity)));
    }

    private Optional<Tag> findDuplicate(TagDto entity){
        return dao.findDistinctByName(entity.getName());
    }

    @Override
    @Transactional
    public void delete(Long id) throws NoSuchEntityException {
        dao.findById(id).ifPresentOrElse(
                e->dao.deleteById(id),
                ()-> {throw new NoSuchEntityException(Tag.class);});
    }

    @Override
    @Transactional
    public void delete(TagDto entity) throws NoSuchEntityException {
        delete(entity.getId());
    }

    @Override
    public TagDto getByName(String name) {
        return mapper.map(dao.findDistinctByName(name).orElseThrow(() -> new NoSuchEntityException(Tag.class)));
    }

    @Override
    @Transactional
    public TagDto retrieveMostUsedTagOfUserWithLargestOrderCost() {
        return mapper.map(dao.findMostUsedTagOfUserWithHighestOrderCost()
                .orElseThrow(()->new NoSuchEntityException(Tag.class)));
    }
}
