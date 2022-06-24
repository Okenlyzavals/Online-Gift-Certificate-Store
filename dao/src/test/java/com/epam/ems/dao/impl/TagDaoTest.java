package com.epam.ems.dao.impl;

import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.config.TestDbConfig;
import com.epam.ems.dao.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {TestDbConfig.class},
        loader = AnnotationConfigContextLoader.class)
@DataJpaTest
@EntityScan(basePackages = "com.epam.ems.*")
class TagDaoTest {

    @Autowired
    TagDao dao;

    @Test
    void testGetAll(){
        List<Tag> expectedTags = List.of(
                new Tag(1L,"clammish", null),
                new Tag(2L,"subproofs", null),
                new Tag(3L,"gesticulacious", null),
                new Tag(4L,"inadjustability", null),
                new Tag(5L,"curryfavour", null),
                new Tag(6L,"high-blazing", null),
                new Tag(7L,"put-out", null),
                new Tag(8L,"twin-tractor", null),
                new Tag(9L,"endomysium", null),
                new Tag(10L,"murthering", null)
        );
        List<Tag> actualTags = dao.findAll(PageRequest.of(0,10)).toList();

        assertTrue(expectedTags.size() == actualTags.size()
                && expectedTags.containsAll(actualTags)
                && actualTags.containsAll(expectedTags));
    }


    @Test
    void testGetByExistingId(){
        Optional<Tag> expected = Optional.of(new Tag(1L,"clammish",null));
        Optional<Tag> actual = dao.findById(1L);

        assertEquals(expected,actual);
    }
    @Test
    void testGetByNonExistingId(){
        Optional<Tag> expected = Optional.empty();
        Optional<Tag> actual = dao.findById(-5L);

        assertEquals(expected,actual);
    }

    @Test
    void testGetByExistingName(){

        Optional<Tag> expected = Optional.of(new Tag(1L,"clammish",null));
        Optional<Tag> actual = dao.findDistinctByName("clammish");

        assertEquals(expected,actual);
    }


    @Test
    void testGetByNotExistingName(){
        Optional<Tag> expected = Optional.empty();
        Optional<Tag> actual = dao.findDistinctByName("Cooks");

        assertEquals(expected,actual);
    }

    @Test
    @Transactional
    void testCreateNewTag(){
        Tag expected = new Tag(null,"testing", null);

        Tag actual = dao.save(expected);
        expected.setId(11L);

        assertEquals(expected, actual);
    }

    @Test
    void testCreateDuplicateTag(){
        Tag duplicate = new Tag(null,"clammish", null);

        assertThrows(DataIntegrityViolationException.class, ()->dao.save(duplicate));
    }


    @Test
    void testDeleteByCorrectId(){
        Tag toDelete = new Tag(1L,"clammish",null);

        dao.deleteById(toDelete.getId());
        Optional<Tag> actual = dao.findById(toDelete.getId());

        assertTrue(actual.isEmpty());

    }

    @Test
    void testDeleteByIncorrectId(){
        long deletionId = -12024L;

        assertThrows(EmptyResultDataAccessException.class, ()->dao.deleteById(deletionId));
    }
}
