package com.epam.ems.dao.impl;

import com.epam.ems.dao.config.H2DbConfig;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.dao.impl.SqlTagDao;
import com.epam.ems.dao.rowmapper.TagRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SqlTagDaoTest {

    private SqlTagDao tagDao;
    private TagRowMapper rowMapper;
    private DataSource dataSource;
    private JdbcTemplate template;

    @BeforeEach
    void initDatabase(){
        rowMapper = new TagRowMapper();
        dataSource = new H2DbConfig().h2DataSource();
        tagDao = new SqlTagDao(dataSource, rowMapper);
        template = new JdbcTemplate(dataSource);
    }

    @Test
    void testGetAll(){
        List<Tag> expected = List.of(
                new Tag(1L,"Java"), new Tag(2L,"Books"),
                new Tag(3L,"Programming"),new Tag(4L,"Professional"),
                new Tag(5L,"C language"),new Tag(6L,"C#"),
                new Tag(7L,"C++"),new Tag(8L,"Movies"),
                new Tag(9L,"TV series"),new Tag(10L,"Cartoons"),
                new Tag(11L,"Groceries"),new Tag(12L,"Walmart"));

        List<Tag> actual = tagDao.retrieveAll();
        actual.sort(Comparator.comparing(Tag::getId));

        assertEquals(expected, actual);
    }

    @Test
    void testGetByExistingId(){
        Optional<Tag> expected = Optional.of(new Tag(1L,"Java"));
        Optional<Tag> actual = tagDao.retrieveById(1L);

        assertEquals(expected,actual);
    }

    @Test
    void testGetByNonExistingId(){
        Optional<Tag> expected = Optional.empty();
        Optional<Tag> actual = tagDao.retrieveById(-5L);

        assertEquals(expected,actual);
    }

    @Test
    void testGetByExistingName(){

        Optional<Tag> expected = Optional.of(new Tag(2L,"Books"));
        Optional<Tag> actual = tagDao.findByName("Books");

        assertEquals(expected,actual);
    }


    @Test
    void testGetByNotExistingName(){
        Optional<Tag> expected = Optional.empty();
        Optional<Tag> actual = tagDao.findByName("Cooks");

        assertEquals(expected,actual);
    }

    @Test
    void testCreateNewTag(){
        Tag expected = new Tag(13L,"Software testing");
        Tag actual = template.queryForObject(
                "SELECT * FROM tag WHERE tag_id=?",
                rowMapper,
                tagDao.create(expected));

        assertEquals(expected, actual);
    }

    @Test
    void testCreateDuplicateTag(){
        Tag duplicate = new Tag(1L,"Java");

        assertThrows(DataAccessException.class, ()->tagDao.create(duplicate));
    }

    @Test
    void testDeleteByCorrectId(){
        long deletionId = 1L;
        tagDao.delete(deletionId);

        assertThrows(IncorrectResultSizeDataAccessException.class,
                ()-> {
                    template.queryForObject(
                            "SELECT * FROM tag WHERE tag_id=?",
                            rowMapper,
                            deletionId);
                });
    }

    @Test
    void testDeleteByIncorrectId(){
        long deletionId = -12024L;
        int expectedRowsCount = 12;

        tagDao.delete(deletionId);

        int actualRowsCount = template.queryForObject("select count(*) from tag", Integer.class);

        assertEquals(expectedRowsCount, actualRowsCount);
    }

    @Test
    void testDeleteExistingEntity(){
        Tag toDelete = new Tag(1L,"Java");
        tagDao.delete(toDelete);

        assertThrows(IncorrectResultSizeDataAccessException.class,
                ()-> {
                    template.queryForObject(
                            "SELECT * FROM tag WHERE tag_id=?",
                            rowMapper,
                            toDelete.getId());
                });
    }

    @Test
    void deleteNotExistingEntity(){
        Tag toDelete = new Tag(-124151L, "абырвалг");
        int expectedRowsCount = 12;

        tagDao.delete(toDelete);

        int actualRowsCount = template.queryForObject("select count(*) from tag", Integer.class);

        assertEquals(expectedRowsCount, actualRowsCount);
    }

}
