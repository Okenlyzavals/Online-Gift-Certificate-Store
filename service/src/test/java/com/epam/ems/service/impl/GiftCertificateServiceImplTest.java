package com.epam.ems.service.impl;

import com.epam.ems.dao.GiftCertificateDao;
import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.dto.TagDto;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.mapper.impl.GiftCertificateDtoMapper;
import com.epam.ems.service.mapper.impl.TagDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Spy
    private GiftCertificateDtoMapper mapper = new GiftCertificateDtoMapper(new TagDtoMapper());
    @Mock
    private GiftCertificateDao dao;
    @Mock
    private TagDao tagDao;

    @InjectMocks
    private GiftCertificateServiceImpl service;

    @Test
    void testGetAll(){
        List<GiftCertificate> certificateList = List.of(
                GiftCertificate.builder().id(1L).tags(new HashSet<>()).name("Cert one").build(),
                GiftCertificate.builder().id(2L).tags(new HashSet<>()).name("Cert two").build(),
                GiftCertificate.builder().id(3L).tags(new HashSet<>()).name("Cert+tags")
                        .tags(Set.of(new Tag(1L,"tag",null), new Tag(2L,"tyg",null))).build());
        List<GiftCertificateDto> expected = certificateList
                .stream()
                .map(e->new GiftCertificateDto(e.getId(),
                        e.getName(), e.getDescription(),
                        e.getPrice(), e.getDuration(), e.getCreateDate(),
                        e.getLastUpdateDate(),
                        e.getTags().stream().map(o->new TagDto(o.getId(), o.getName()))
                                .collect(Collectors.toSet())))
                .collect(Collectors.toList());

        when(dao.findAll((Pageable) any())).thenReturn(new PageImpl<>(certificateList));

        List<GiftCertificateDto> actual = service.getAll(5,5).toList();
        assertEquals(expected, actual);
    }

    @Test
    void testGetByCorrectId(){
        GiftCertificateDto expected = new GiftCertificateDto(15L, "cert", null,null,null,null,null,null);

        when(dao.findById(15L)).thenReturn(Optional.of(GiftCertificate.builder().id(15L).name("cert").build()));

        GiftCertificateDto actual = service.getById(15L);
        assertEquals(expected, actual);
    }

    @Test
    void testGetByIncorrectId(){
        when(dao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, ()->service.getById(15L));
    }

    @Test
    void testInsertNewEntityWithoutTags(){
        when(dao.save(any())).thenReturn(null);
        assertDoesNotThrow(()->service.insert(new GiftCertificateDto()));
    }

    @Test
    void testInsertNewEntityWithTags(){
        GiftCertificateDto toInsert = new GiftCertificateDto();
        toInsert.setTags(Set.of(new TagDto(null,"tag"), new TagDto(2L,"tug")));

        when(dao.save(any())).thenReturn(null);

        assertDoesNotThrow(()->service.insert(toInsert));
    }

    @Test
    void testDeleteByExistingId(){
        when(dao.findById(anyLong())).thenReturn(Optional.of(GiftCertificate.builder().build()));
        assertDoesNotThrow(()->service.delete(1L));
    }

    @Test
    void testDeleteByMissingId(){
        when(dao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, ()->service.delete(1L));
    }

    @Test
    void testDeleteExistingEntity(){
        GiftCertificateDto toDelete = new GiftCertificateDto();
        toDelete.setId(1L);

        when(dao.findById(anyLong())).thenReturn(Optional.of(GiftCertificate.builder().build()));

        assertDoesNotThrow(()->service.delete(toDelete));
    }

    @Test
    void testDeleteMissingEntity(){
        GiftCertificateDto toDelete = new GiftCertificateDto();
        toDelete.setId(1L);

        when(dao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityException.class, ()->service.delete(toDelete));
    }

    @Test
    void retrieveByCorrectCriteriaTest(){
        Map<String,Object> criteria = new LinkedHashMap<>();
        criteria.put("ORDER_BY_NAME_ASC","");
        criteria.put("TAG_NAME","tag");
        List<GiftCertificateDto> expected = List.of(new GiftCertificateDto());

        when(dao.retrieveByCriteria(any(), any())).thenReturn(new PageImpl<>(List.of(GiftCertificate.builder().build())));

        assertEquals(expected, service.getByCriteria(criteria,1,1).toList());
    }

    @Test
    void updateNonExistingTest(){
        GiftCertificateDto toUpdate = new GiftCertificateDto();
        toUpdate.setId(1L);

        when(dao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, ()->service.update(toUpdate));
    }


    @Test
    void updateExistingWithTagsTest(){
        GiftCertificateDto toUpdate = new GiftCertificateDto();
        toUpdate.setId(1L);
        toUpdate.setTags(Set.of(new TagDto(null,"tag"), new TagDto(2L,"tug")));

        when(dao.findById(anyLong())).thenReturn(Optional.of(GiftCertificate.builder().build()));

        assertDoesNotThrow(()->service.update(toUpdate));
    }
}
