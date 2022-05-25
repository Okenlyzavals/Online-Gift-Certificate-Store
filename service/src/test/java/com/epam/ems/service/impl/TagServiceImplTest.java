package com.epam.ems.service.impl;

import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.service.dto.TagDto;
import com.epam.ems.service.exception.DuplicateEntityException;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.mapper.impl.TagDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagDao dao;
    @Mock
    private TagDtoMapper mapper;

    @InjectMocks
    private TagServiceImpl service;

    @Test
    void testGetAll(){
        List<Tag> taglist = List.of(new Tag(1L,"tag1",null),
                new Tag(2L,"Tag0",null), new Tag(3L, "TAG",null));
        List<TagDto> expected = taglist.stream().map(e->new TagDto(e.getId(), e.getName())).collect(Collectors.toList());

        when(dao.retrieveAll(anyInt(),anyInt())).thenReturn(taglist);
        when(mapper.map(any())).thenCallRealMethod();
        List<TagDto> actual = service.getAll(5,5);

        assertEquals(expected, actual);

    }

    @Test
    void testGetByCorrectId(){
        Tag toReturn = new Tag(15L,"got it",null);
        TagDto expected = new TagDto(15L,"got it");

        when(dao.retrieveById(15L)).thenReturn(Optional.of(toReturn));
        when(mapper.map(any())).thenCallRealMethod();
        TagDto actual = service.getById(15L);

        assertEquals(expected, actual);
    }

    @Test
    void testGetByIncorrectId(){
        when(dao.retrieveById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, ()->service.getById(1L));
    }

    @Test
    void testGetByCorrectName(){
        Tag toReturn = new Tag(15L,"this  name",null);
        TagDto expected = new TagDto(15L,"this  name");

        when(dao.findByName("this name")).thenReturn(Optional.of(toReturn));
        when(mapper.map(any())).thenCallRealMethod();
        TagDto actual = service.getByName("this name");

        assertEquals(expected, actual);
    }


    @Test
    void testGetByIncorrectName(){
        when(dao.findByName(any())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, ()->service.getByName("that name"));
    }

    @Test
    void testInsertNewEntity(){
        TagDto toCreate = new TagDto(13L,"Like a man is the mountainside");
        when(dao.create(any())).thenReturn(new Tag());

        assertDoesNotThrow(()->service.insert(toCreate));
    }

    @Test
    void testInsertDuplicateEntity(){
        TagDto duplicate = new TagDto(13L,"Like a man is the mountainside");
        when(dao.findByName(anyString())).thenReturn(Optional.of(Tag.builder().id(1L).build()));

        assertThrows(DuplicateEntityException.class, ()->service.insert(duplicate));
    }

    @Test
    void testDeleteByExistingId(){
        when(dao.retrieveById(anyLong())).thenReturn(Optional.of(Tag.builder().build()));
        assertDoesNotThrow(()->service.delete(1L));
    }

    @Test
    void testDeleteByMissingId(){
        when(dao.retrieveById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, ()->service.delete(1L));
    }

    @Test
    void testDeleteExistingEntity(){
        when(dao.retrieveById(anyLong())).thenReturn(Optional.of(Tag.builder().build()));
        assertDoesNotThrow(()->service.delete(new TagDto(1L,"")));
    }

    @Test
    void testDeleteMissingEntity(){
        when(dao.retrieveById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, ()->service.delete(new TagDto(1L,"")));
    }
}
