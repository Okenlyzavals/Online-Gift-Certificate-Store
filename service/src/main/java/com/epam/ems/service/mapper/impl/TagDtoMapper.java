package com.epam.ems.service.mapper.impl;

import com.epam.ems.dao.entity.Tag;
import com.epam.ems.service.dto.TagDto;
import com.epam.ems.service.mapper.Mapper;
import org.springframework.stereotype.Component;

/**
 * Extension of {@link Mapper} suited for
 * {@link Tag} Entity and {@link TagDto} DTO.
 *
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
@Component
public class TagDtoMapper implements Mapper<Tag, TagDto> {

    @Override
    public TagDto map(Tag entity) {
        if(entity == null){
            return new TagDto();
        }
        return new TagDto(entity.getId(), entity.getName());
    }

    @Override
    public Tag extract(TagDto dto) {
        if (dto == null) {
            return Tag.builder().build();
        }
        return Tag.builder().id(dto.getId()).name(dto.getName()).build();
    }
}
