package com.epam.ems.service.mapper.impl;

import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.dto.TagDto;
import com.epam.ems.service.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GiftCertificateDtoMapper implements Mapper<GiftCertificate, GiftCertificateDto> {

    private final Mapper<Tag, TagDto> tagMapper;

    @Autowired
    public GiftCertificateDtoMapper(Mapper<Tag, TagDto> tagMapper){
        this.tagMapper = tagMapper;
    }

    @Override
    public GiftCertificateDto map(GiftCertificate certificate){
        if(certificate == null){
            return new GiftCertificateDto();
        }
        Set<TagDto> tagDtoList = (certificate.getTags() != null)
                ? certificate.getTags()
                    .stream()
                    .map(tagMapper::map)
                    .collect(Collectors.toSet())
                : null;

        return new GiftCertificateDto(
                certificate.getId(),
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                certificate.getCreateDate(),
                certificate.getLastUpdateDate(),
                tagDtoList);

    }

    @Override
    public GiftCertificate extract(GiftCertificateDto dto){
        if(dto == null){
            return GiftCertificate.builder().build();
        }

        Set<Tag> tags = (dto.getTags() != null)
                ? dto.getTags().stream()
                    .map(tagMapper::extract)
                    .collect(Collectors.toSet())
                : null;

        return GiftCertificate.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .price(dto.getPrice())
                .createDate(dto.getCreateDate())
                .lastUpdateDate(dto.getLastUpdateDate())
                .tags(tags).build();
    }

}
