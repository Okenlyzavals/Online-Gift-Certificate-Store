package com.epam.ems.dao.impl;

import com.epam.ems.dao.config.H2DbConfig;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.dao.entity.criteria.Criteria;
import com.epam.ems.dao.querybuilder.CriteriaQueryBuilder;
import com.epam.ems.dao.querybuilder.UpdateQueryBuilder;
import com.epam.ems.dao.querybuilder.UpdateQueryBuilderTest;
import com.epam.ems.dao.rowmapper.CertificateRowMapper;
import com.epam.ems.dao.rowmapper.TagRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SqlGiftCertificateDaoTest {

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final List<GiftCertificate> allCertsList;

    private SqlGiftCertificateDao certDao;
    private CertificateRowMapper rowMapper;
    private UpdateQueryBuilder updateBuilder;
    private CriteriaQueryBuilder criteriaBuilder;

    private SqlTagDao tagDao;
    private TagRowMapper tagRowMapper;

    private DataSource dataSource;
    private JdbcTemplate template;

    static {
        allCertsList = List.of(
                new GiftCertificate(
                        1L,
                        "Programming book store certificate",
                        "Acquire new Java, C#, C, or C++ books to develop your programming skills.",
                        new BigDecimal("100.00000"),
                        30,
                        LocalDateTime.parse("2022-04-02 12:50:50",format),
                        LocalDateTime.parse("2022-04-06 16:16:31", format),
                        List.of(new Tag(1L,"Java"), new Tag(2L,"Books"),
                                new Tag(3L,"Programming"),new Tag(4L,"Professional"),
                                new Tag(5L,"C language"),new Tag(6L,"C#"),
                                new Tag(7L,"C++"))),
                new GiftCertificate(2L,
                        "Alpha movie store",
                        null,
                        new BigDecimal("125.00000"),
                        7,
                        LocalDateTime.parse("2022-04-02 15:19:42",format),
                        LocalDateTime.parse("2022-04-02 15:19:42",format),
                        List.of(new Tag(8L,"Movies"), new Tag(9L,"TV series"),
                                new Tag(10L,"Cartoons"))),
                new GiftCertificate(
                        3L,
                        "Grocery store certificate",
                        "Walmart special gift certificate for yall ninjas",
                        new BigDecimal("45.50000"),
                        21,
                        LocalDateTime.parse("2022-04-06 16:25:00",format),
                        LocalDateTime.parse("2022-04-06 16:26:40",format),
                        List.of(new Tag(11L,"Groceries"),new Tag(12L,"Walmart")))
        );
    }

    @BeforeEach
    void initDatabase(){
        tagRowMapper = new TagRowMapper();
        rowMapper = new CertificateRowMapper();
        updateBuilder = new UpdateQueryBuilder();
        criteriaBuilder = new CriteriaQueryBuilder();

        dataSource = new H2DbConfig().h2DataSource();
        tagDao = new SqlTagDao(dataSource, tagRowMapper);
        certDao = new SqlGiftCertificateDao(dataSource, rowMapper, criteriaBuilder, updateBuilder,tagDao);
        template = new JdbcTemplate(dataSource);
    }

    @Test
    void testGetAll(){
        List<GiftCertificate> expected = new ArrayList<>(allCertsList);
        List<GiftCertificate> actual = certDao.retrieveAll();

        actual.sort(Comparator.comparingLong(GiftCertificate::getId));
        actual.stream().peek(e->e.getTags().sort(Comparator.comparingLong(Tag::getId)));

        assertEquals(expected, actual);
    }

    @Test
    void testGetByExistingId(){
        Optional<GiftCertificate> expected = Optional.of( new GiftCertificate(
                3L,
                "Grocery store certificate",
                "Walmart special gift certificate for yall ninjas",
                new BigDecimal("45.50000"),
                21,
                LocalDateTime.parse("2022-04-06 16:25:00",format),
                LocalDateTime.parse("2022-04-06 16:26:40",format),
                List.of(new Tag(11L,"Groceries"),new Tag(12L,"Walmart"))));
        Optional<GiftCertificate> actual = certDao.retrieveById(3L);

        assertEquals(expected, actual);
    }

    @Test
    void testGetByNonExistingId(){
        Optional<GiftCertificate> expected = Optional.empty();
        Optional<GiftCertificate> actual = certDao.retrieveById(-14124124L);

        assertEquals(expected, actual);
    }

    @Test
    void testCreateNewCertificate(){
        GiftCertificate toCreate =  new GiftCertificate(
                4L,
                "Spare part store certificate",
                "Go get a new carburetor for your ol' VAZ six.",
                new BigDecimal("350.00000"),
                60,
                LocalDateTime.parse("2022-03-12 17:00:05",format),
                LocalDateTime.parse("2022-04-13 15:06:40",format),
                List.of());

        long newId = certDao.create(toCreate);

        GiftCertificate created
                = template.queryForObject("SELECT * FROM gift_certificate WHERE id=?", rowMapper, newId);

        assertEquals(toCreate, created);
    }

    @Test
    void testCreateDuplicateCertificate(){
        GiftCertificate toCreate = allCertsList.get(0);

        assertDoesNotThrow(()->{
            certDao.create(toCreate);
        });
    }

    @Test
    void testDeleteByCorrectId(){
        long deletionId = 1L;
        certDao.delete(deletionId);

        assertThrows(IncorrectResultSizeDataAccessException.class,
                ()-> {
                    template.queryForObject(
                            "SELECT * FROM gift_certificate WHERE id=?",
                            rowMapper,
                            deletionId);
                });
    }

    @Test
    void testDeleteByIncorrectId(){
        long deletionId = -12024L;
        int expectedRowsCount = 3;

        certDao.delete(deletionId);

        int actualRowsCount = template.queryForObject("select count(*) from gift_certificate", Integer.class);

        assertEquals(expectedRowsCount, actualRowsCount);
    }

    @Test
    void testDeleteExistingEntity(){
        GiftCertificate toDelete = new GiftCertificate(
                3L,
                "Grocery store certificate",
                "Walmart special gift certificate for yall ninjas",
                new BigDecimal("45.50000"),
                21,
                LocalDateTime.parse("2022-04-06 16:25:00",format),
                LocalDateTime.parse("2022-04-06 16:26:40",format),
                List.of(new Tag(11L,"Groceries"),new Tag(12L,"Walmart")));

        certDao.delete(toDelete);

        assertThrows(IncorrectResultSizeDataAccessException.class,
                ()-> {
                    template.queryForObject(
                            "SELECT * FROM gift_certificate WHERE id=?",
                            rowMapper,
                            toDelete.getId());
                });
    }

    @Test
    void testDeleteNotExistingEntity(){
        GiftCertificate toDelete = GiftCertificate.builder().id(141L).name("Certift").build();
        int expectedRowsCount = 3;

        certDao.delete(toDelete);

        int actualRowsCount = template.queryForObject("select count(*) from gift_certificate", Integer.class);

        assertEquals(expectedRowsCount, actualRowsCount);
    }

    @Test
    void testUpdateCorrectParams(){
        GiftCertificate expected = new GiftCertificate(
                3L,
                "Walmart certificate",
                "Walmart special gift certificate for yall ninjas",
                new BigDecimal("45.50000"),
                38,
                LocalDateTime.parse("2022-04-06 16:25:00",format),
                LocalDateTime.parse("2022-04-13 15:45:23",format),
                List.of());

        GiftCertificate toUpdate = GiftCertificate.builder()
                .id(3L)
                .name("Walmart certificate")
                .lastUpdateDate(LocalDateTime.parse("2022-04-13 15:45:23",format))
                .duration(38)
                .build();

        certDao.update(toUpdate);

        GiftCertificate actual = template.queryForObject(
                "SELECT * FROM gift_certificate WHERE id=?",
                rowMapper,
                3L);

        assertEquals(expected, actual);
    }

    @Test
    void testUpdateIncorrectParams(){

        GiftCertificate toUpdate = GiftCertificate.builder()
                .id(3L)
                .name("This is toooooooooooooooo looooooooooooooooooooooooooooooooooooooooooooooooonggggggg")
                .build();

        assertThrows(DataAccessException.class, ()-> certDao.update(toUpdate));
    }

    @Test
    void  testRetrieveTagsFromCertById(){
        List<Tag> expected = List.of(new Tag(11L,"Groceries"),new Tag(12L,"Walmart"));
        List<Tag> actual = tagDao.retrieveTagsByCertificateId(3L);

        assertEquals(expected, actual);

    }

    @Test
    void  testRetrieveByCriteriaOrderByDate(){
        List<GiftCertificate> expected = new ArrayList<>(allCertsList);
        expected.sort(Comparator.comparing(GiftCertificate::getCreateDate));

        Criteria criteria = new Criteria();
        criteria.put(Criteria.ParamName.ORDER_DATE_ASC,"");

        List<GiftCertificate> actual = certDao.retrieveByCriteria(criteria);

        assertEquals(expected, actual);
    }

    @Test
    void  testRetrieveByCriteriaOrderByName(){
        List<GiftCertificate> expected = new ArrayList<>(allCertsList);
        expected.sort(Comparator.comparing(GiftCertificate::getName));

        Criteria criteria = new Criteria();
        criteria.put(Criteria.ParamName.ORDER_NAME_ASC,"");

        List<GiftCertificate> actual = certDao.retrieveByCriteria(criteria);
        assertEquals(expected, actual);
    }

    @Test
    void  testRetrieveByCriteriaOrderByDateAndName(){
        List<GiftCertificate> expected = new ArrayList<>(allCertsList);
        expected.sort((o1,o2)->{
            if(o1.getName().compareTo(o2.getName()) != 0){
                return o1.getName().compareTo(o2.getName());
            }

            return o2.getCreateDate().compareTo(o1.getCreateDate());
        });

        Criteria criteria = new Criteria();
        criteria.put(Criteria.ParamName.ORDER_NAME_ASC,"");
        criteria.put(Criteria.ParamName.ORDER_DATE_DESC,"");

        List<GiftCertificate> actual = certDao.retrieveByCriteria(criteria);
        assertEquals(expected, actual);
    }

    @Test
    void  testRetrieveByCriteriaByTag(){
        List<GiftCertificate> expected = List.of(new GiftCertificate(
                3L,
                "Grocery store certificate",
                "Walmart special gift certificate for yall ninjas",
                new BigDecimal("45.50000"),
                21,
                LocalDateTime.parse("2022-04-06 16:25:00",format),
                LocalDateTime.parse("2022-04-06 16:26:40",format),
                List.of(new Tag(11L,"Groceries"),new Tag(12L,"Walmart"))));

        Criteria criteria = new Criteria();
        criteria.put(Criteria.ParamName.TAG_NAME, "Groceries");

        List<GiftCertificate> actual = certDao.retrieveByCriteria(criteria);

        assertEquals(expected, actual);
    }

    @Test
    void  testRetrieveByCriteriaByNameContains(){
        List<GiftCertificate> expected = List.of(new GiftCertificate(2L,
                "Alpha movie store",
                null,
                new BigDecimal("125.00000"),
                7,
                LocalDateTime.parse("2022-04-02 15:19:42",format),
                LocalDateTime.parse("2022-04-02 15:19:42",format),
                List.of(new Tag(8L,"Movies"), new Tag(9L,"TV series"),
                        new Tag(10L,"Cartoons"))));

        Criteria criteria = new Criteria();
        criteria.put(Criteria.ParamName.NAME_CONTAINS, "movie");

        CriteriaQueryBuilder builder = new CriteriaQueryBuilder();
        builder.parse(criteria);
        List<GiftCertificate> actual = certDao.retrieveByCriteria(criteria);

        assertEquals(expected, actual);
    }


    @Test
    void  testRetrieveByCriteriaByDescContains(){
        List<GiftCertificate> expected = List.of(new GiftCertificate(
                3L,
                "Grocery store certificate",
                "Walmart special gift certificate for yall ninjas",
                new BigDecimal("45.50000"),
                21,
                LocalDateTime.parse("2022-04-06 16:25:00",format),
                LocalDateTime.parse("2022-04-06 16:26:40",format),
                List.of(new Tag(11L,"Groceries"),new Tag(12L,"Walmart"))));

        Criteria criteria = new Criteria();
        criteria.put(Criteria.ParamName.DESCRIPTION_CONTAINS, "ninja");

        List<GiftCertificate> actual = certDao.retrieveByCriteria(criteria);

        assertEquals(expected, actual);
    }

}
