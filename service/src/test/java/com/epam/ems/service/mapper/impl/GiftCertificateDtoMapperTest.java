package com.epam.ems.service.mapper.impl;

import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.dto.TagDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GiftCertificateDtoMapperTest {

    private final GiftCertificateDtoMapper mapper = new GiftCertificateDtoMapper();

    @Test
    void testMapEmpty(){
        GiftCertificateDto expected = new GiftCertificateDto();

        GiftCertificateDto actual = mapper.map(null);

        assertEquals(expected, actual);
    }

    @Test
    void testExtractEmpty(){
        GiftCertificate expected = GiftCertificate.builder().build();

        GiftCertificate actual = mapper.extract(null);

        assertEquals(expected, actual);
    }

    @Test
    void testMapCorrectWithTags(){
        GiftCertificate toMap = GiftCertificate.builder()
                .id(13L)
                .name("test cert")
                .tags(List.of(new Tag(1L,"tag no1"), new Tag(2L, "tag no2")))
                .build();

        GiftCertificateDto expected = new GiftCertificateDto(
                13L,"test cert",null,null,null,
                null,null,
                List.of(new TagDto(1L,"tag no1"), new TagDto(2L, "tag no2")));

        GiftCertificateDto actual = mapper.map(toMap);

        assertEquals(expected, actual);
    }

    @Test
    void testExtractCorrectWithTags(){
        GiftCertificateDto toExtract = new GiftCertificateDto(
                13L,"test cert",null,null,null,
                null,null,
                List.of(new TagDto(1L,"tag no1"), new TagDto(2L, "tag no2")));

        GiftCertificate expected = GiftCertificate.builder()
                .id(13L)
                .name("test cert")
                .tags(List.of(new Tag(1L,"tag no1"), new Tag(2L, "tag no2")))
                .build();

        GiftCertificate actual = mapper.extract(toExtract);

        assertEquals(expected, actual);
    }

    @Test
    void testMapCorrectNoTags(){
        GiftCertificate toMap = GiftCertificate.builder()
                .id(13L)
                .name("test cert")
                .build();

        GiftCertificateDto expected = new GiftCertificateDto(
                13L,"test cert",null,null,null,
                null,null, null);

        GiftCertificateDto actual = mapper.map(toMap);

        assertEquals(expected, actual);
    }

    @Test
    void testExtractCorrectNoTags(){
        GiftCertificateDto toExtract = new GiftCertificateDto(
                13L,"test cert",null,null,null,
                null,null,
                null);

        GiftCertificate expected = GiftCertificate.builder()
                .id(13L)
                .name("test cert")
                .build();

        GiftCertificate actual = mapper.extract(toExtract);

        assertEquals(expected, actual);
    }
}
