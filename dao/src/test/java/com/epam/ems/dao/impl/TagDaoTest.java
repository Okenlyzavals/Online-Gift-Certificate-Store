package com.epam.ems.dao.impl;

import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.config.TestDbConfig;
import com.epam.ems.dao.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TagDaoImpl.class, TestDbConfig.class}, loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class TagDaoTest {

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
        List<Tag> actualTags = dao.retrieveAll(1,10);

        assertTrue(expectedTags.size() == actualTags.size()
                && expectedTags.containsAll(actualTags)
                && actualTags.containsAll(expectedTags));
    }


    @Test
    void testGetByExistingId(){
        Optional<Tag> expected = Optional.of(new Tag(1L,"clammish",null));
        Optional<Tag> actual = dao.retrieveById(1L);

        assertEquals(expected,actual);
    }
    @Test
    void testGetByNonExistingId(){
        Optional<Tag> expected = Optional.empty();
        Optional<Tag> actual = dao.retrieveById(-5L);

        assertEquals(expected,actual);
    }

    @Test
    void testGetByExistingName(){

        Optional<Tag> expected = Optional.of(new Tag(1L,"clammish",null));
        Optional<Tag> actual = dao.findByName("clammish");

        assertEquals(expected,actual);
    }


    @Test
    void testGetByNotExistingName(){
        Optional<Tag> expected = Optional.empty();
        Optional<Tag> actual = dao.findByName("Cooks");

        assertEquals(expected,actual);
    }

    @Test
    @Transactional
    void testCreateNewTag(){
        Tag expected = new Tag(null,"testing", null);

        Tag actual = dao.create(expected);
        expected.setId(11L);

        assertEquals(expected, actual);
    }

    @Test
    void testCreateDuplicateTag(){
        Tag duplicate = new Tag(null,"clammish", null);

        assertThrows(PersistenceException.class, ()->dao.create(duplicate));
    }


    @Test
    void testDeleteByCorrectId(){
        Tag toDelete = new Tag(1L,"clammish",null);

        dao.delete(toDelete.getId());
        Optional<Tag> actual = dao.retrieveById(toDelete.getId());

        assertTrue(actual.isEmpty());

    }

    @Test
    void testDeleteByIncorrectId(){
        long deletionId = -12024L;

        assertThrows(IllegalArgumentException.class, ()->dao.delete(deletionId));
    }
}
