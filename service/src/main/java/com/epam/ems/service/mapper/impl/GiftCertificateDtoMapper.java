package com.epam.ems.service.mapper.impl;

import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.dto.TagDto;
import com.epam.ems.service.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GiftCertificateDtoMapper implements Mapper<GiftCertificate, GiftCertificateDto> {

    @Override
    public GiftCertificateDto map(GiftCertificate certificate){
        if(certificate == null){
            return new GiftCertificateDto();
        }
        List<TagDto> tagDtoList = (certificate.getTags() != null)
                ? certificate.getTags()
                    .stream()
                    .map(e-> new TagDto(e.getId(), e.getName()))
                    .collect(Collectors.toList())
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

        List<Tag> tags = (dto.getTags() != null)
                ? dto.getTags().stream()
                    .map(e -> Tag.builder().id(e.getId()).name(e.getName()).build())
                    .collect(Collectors.toList())
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
