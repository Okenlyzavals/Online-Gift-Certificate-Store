package com.epam.ems.service.impl;

import com.epam.ems.dao.GiftCertificateDao;
import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.dto.TagDto;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.mapper.impl.GiftCertificateDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateDao dao;
    @Mock
    private TagDao tagDao;
    @Mock
    private GiftCertificateDtoMapper mapper;

    @InjectMocks
    private GiftCertificateServiceImpl service;

    @Test
    void testGetAll(){
        List<GiftCertificate> certificateList = List.of(
                GiftCertificate.builder().id(1L).tags(new ArrayList<>()).name("Cert one").build(),
                GiftCertificate.builder().id(2L).tags(new ArrayList<>()).name("Cert two").build(),
                GiftCertificate.builder().id(3L).tags(new ArrayList<>()).name("Cert+tags")
                        .tags(List.of(new Tag(1L,"tag"), new Tag(2L,"tyg"))).build());
        List<GiftCertificateDto> expected = certificateList
                .stream()
                .map(e->new GiftCertificateDto(e.getId(),
                        e.getName(), e.getDescription(),
                        e.getPrice(), e.getDuration(), e.getCreateDate(),
                        e.getLastUpdateDate(),
                        e.getTags().stream().map(o->new TagDto(o.getId(), o.getName()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());

        when(dao.retrieveAll()).thenReturn(certificateList);
        when(mapper.map(any())).thenCallRealMethod();

        List<GiftCertificateDto> actual = service.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void testGetByCorrectId(){
        GiftCertificateDto expected = new GiftCertificateDto(15L, "cert", null,null,null,null,null,null);

        when(dao.retrieveById(15L)).thenReturn(Optional.of(GiftCertificate.builder().id(15L).name("cert").build()));
        when(mapper.map(any())).thenCallRealMethod();

        GiftCertificateDto actual = service.getById(15L);
        assertEquals(expected, actual);
    }

    @Test
    void testGetByIncorrectId(){
        when(dao.retrieveById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, ()->service.getById(15L));
    }

    @Test
    void testInsertNewEntityWithoutTags(){
        when(dao.create(any())).thenReturn(1L);
        when(mapper.extract(any())).thenCallRealMethod();
        assertDoesNotThrow(()->service.insert(new GiftCertificateDto()));
    }

    @Test
    void testInsertNewEntityWithTags(){
        GiftCertificateDto toInsert = new GiftCertificateDto();
        toInsert.setTags(List.of(new TagDto(null,"tag"), new TagDto(2L,"tug")));

        when(dao.create(any())).thenReturn(1L);
        when(tagDao.create(any())).thenReturn(0L);
        when(mapper.extract(any())).thenCallRealMethod();

        assertDoesNotThrow(()->service.insert(toInsert));
    }

    @Test
    void testDeleteByExistingId(){
        when(dao.retrieveById(anyLong())).thenReturn(Optional.of(GiftCertificate.builder().build()));
        assertDoesNotThrow(()->service.delete(1L));
    }

    @Test
    void testDeleteByMissingId(){
        when(dao.retrieveById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, ()->service.delete(1L));
    }

    @Test
    void testDeleteExistingEntity(){
        GiftCertificateDto toDelete = new GiftCertificateDto();
        toDelete.setId(1L);

        when(dao.retrieveById(anyLong())).thenReturn(Optional.of(GiftCertificate.builder().build()));

        assertDoesNotThrow(()->service.delete(toDelete));
    }

    @Test
    void testDeleteMissingEntity(){
        GiftCertificateDto toDelete = new GiftCertificateDto();
        toDelete.setId(1L);

        when(dao.retrieveById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityException.class, ()->service.delete(toDelete));
    }

    @Test
    void retrieveByCorrectCriteriaTest(){
        Map<String,String> criteria = new LinkedHashMap<>();
        criteria.put("ORDER_BY_NAME_ASC","");
        criteria.put("TAG_NAME","tag");
        List<GiftCertificateDto> expected = List.of(new GiftCertificateDto());

        when(dao.retrieveByCriteria(any())).thenReturn(List.of(GiftCertificate.builder().build()));
        when(mapper.map(any())).thenCallRealMethod();

        assertEquals(expected, service.getByCriteria(criteria));
    }

    @Test
    void retrieveByNullCriteriaTest(){

        List<GiftCertificateDto> expected = List.of(new GiftCertificateDto());

        when(dao.retrieveByCriteria(any())).thenReturn(List.of(GiftCertificate.builder().build()));
        when(mapper.map(any())).thenCallRealMethod();

        assertEquals(expected, service.getByCriteria(null));
    }

    @Test
    void updateNonExistingTest(){
        GiftCertificateDto toUpdate = new GiftCertificateDto();
        toUpdate.setId(1L);

        when(dao.retrieveById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, ()->service.update(toUpdate));
    }


    @Test
    void updateExistingWithTagsTest(){
        GiftCertificateDto toUpdate = new GiftCertificateDto();
        toUpdate.setId(1L);
        toUpdate.setTags(List.of(new TagDto(null,"tag"), new TagDto(2L,"tug")));

        when(tagDao.create(any())).thenReturn(0L);
        when(dao.retrieveById(anyLong())).thenReturn(Optional.of(GiftCertificate.builder().build()));
        when(mapper.extract(any())).thenCallRealMethod();

        assertDoesNotThrow(()->service.update(toUpdate));
    }
}
