package com.epam.ems.service.mapper.impl;

import com.epam.ems.dao.entity.Tag;
import com.epam.ems.service.dto.TagDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TagDtoMapperTest {

    private final TagDtoMapper mapper = new TagDtoMapper();

    @Test
    void testMapEmpty(){
        TagDto expected = new TagDto();

        TagDto actual = mapper.map(null);

        assertEquals(expected, actual);
    }

    @Test
    void testExtractEmpty(){
        Tag expected = Tag.builder().build();

        Tag actual = mapper.extract(null);

        assertEquals(expected, actual);
    }

    @Test
    void testMapCorrect(){
        Tag toMap = Tag.builder().id(12L).name("mapped").build();
        TagDto expected = new TagDto(12L,"mapped");

        TagDto actual = mapper.map(toMap);

        assertEquals(expected, actual);
    }

    @Test
    void testExtractCorrect(){
        TagDto toExtract = new TagDto(12L,"extracted");
        Tag expected = Tag.builder().id(12L).name("extracted").build();

        Tag actual = mapper.extract(toExtract);

        assertEquals(expected, actual);
    }
}
