package com.epam.ems.dao.impl;

import com.epam.ems.dao.GiftCertificateDao;
import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.config.TestDbConfig;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.dao.entity.criteria.Criteria;
import com.epam.ems.dao.querybuilder.CriteriaQueryBuilder;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TagDaoImpl.class,
        GiftCertificateDaoImpl.class, TestDbConfig.class,
        CriteriaQueryBuilder.class},
        loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@Transactional
public class GiftCertificateDaoTest {

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    @Autowired
    TagDao tagDao;
    @Autowired
    GiftCertificateDao giftCertificateDao;

    @Test
    void testGetAll(){
        List<GiftCertificate> expected = List.of(
                GiftCertificate.builder()
                        .id(1L).price(new BigDecimal("1690.83"))
                        .duration(11)
                        .createDate(LocalDateTime.from(format.parse("2022-03-08 15:33:15.000000")))
                        .lastUpdateDate(LocalDateTime.from(format.parse("2022-05-22 16:16:01.557016")))
                        .name("fanwise tornado-swept certificate")
                        .description("hooker-out Blase self-abhorring pseudotropine axemaster testing archdeceiver outsetting orientating")
                        .tags(Set.of(new Tag(2L,"subproofs", null), new Tag(6L,"high-blazing", null),
                                new Tag(7L,"put-out", null), new Tag(8L,"twin-tractor", null),
                                new Tag(9L,"endomysium", null), new Tag(10L,"murthering", null))).build(),
                GiftCertificate.builder()
                        .id(2L).price(new BigDecimal("4400.86"))
                        .duration(56)
                        .createDate(LocalDateTime.from(format.parse("2022-04-14 16:56:02.000000")))
                        .lastUpdateDate(LocalDateTime.from(format.parse("2022-05-22 16:16:01.567089")))
                        .name("dimethylanthranilate Hentrich certificate")
                        .description("Lymantriidae shacklebone hematozzoa didactive glorification biochemically analabos anecdotist")
                        .tags(Set.of(new Tag(2L,"subproofs", null), new Tag(6L,"high-blazing", null))).build());

        List<GiftCertificate> actual = giftCertificateDao.retrieveAll(1,10);

        assertTrue(expected.size() == actual.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected));


    }

    @Test
    void testGetByExistingId(){
        Optional<GiftCertificate> expected = Optional.of(GiftCertificate.builder()
                        .id(1L).price(new BigDecimal("1690.83"))
                        .duration(11)
                        .createDate(LocalDateTime.from(format.parse("2022-03-08 15:33:15.000000")))
                        .lastUpdateDate(LocalDateTime.from(format.parse("2022-05-22 16:16:01.557016")))
                        .name("fanwise tornado-swept certificate")
                        .description("hooker-out Blase self-abhorring pseudotropine axemaster testing archdeceiver outsetting orientating")
                        .tags(Set.of(new Tag(2L,"subproofs", null), new Tag(6L,"high-blazing", null),
                                new Tag(7L,"put-out", null), new Tag(8L,"twin-tractor", null),
                                new Tag(9L,"endomysium", null), new Tag(10L,"murthering", null))).build());
        Optional<GiftCertificate> actual = giftCertificateDao.retrieveById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void testGetByNonExistingId(){
        Optional<GiftCertificate> expected = Optional.empty();
        Optional<GiftCertificate> actual = giftCertificateDao.retrieveById(-14124124L);

        assertEquals(expected, actual);
    }

    @Test
    void testCreateNewCertificate(){
        Set<Tag> tags = new HashSet<>(Set.of(tagDao.retrieveById(2L).get(),
                tagDao.retrieveById(1L).get(),
                tagDao.retrieveById(8L).get()));
        GiftCertificate toCreate =  GiftCertificate.builder()
                .price(new BigDecimal("1000.0"))
                .duration(14)
                .createDate(LocalDateTime.from(format.parse("2022-03-08 15:33:15.000000")))
                .lastUpdateDate(LocalDateTime.from(format.parse("2022-05-22 16:16:01.557016")))
                .name("throw me some numbers certificate")
                .description("fits everyone")
                .tags(tags).orders(null).build();

        GiftCertificate actual = giftCertificateDao.create(toCreate);
        toCreate.setId(3L);

        assertEquals(toCreate, actual);
    }

    @Test
    void testCreateDuplicateCertificate(){
        GiftCertificate toCreate = GiftCertificate.builder()
                .id(1L).price(new BigDecimal("1690.83"))
                .duration(11)
                .createDate(LocalDateTime.from(format.parse("2022-03-08 15:33:15.000000")))
                .lastUpdateDate(LocalDateTime.from(format.parse("2022-05-22 16:16:01.557016")))
                .name("fanwise tornado-swept certificate")
                .description("hooker-out Blase self-abhorring pseudotropine axemaster testing archdeceiver outsetting orientating")
                .tags(Set.of(new Tag(2L,"subproofs", null), new Tag(6L,"high-blazing", null),
                        new Tag(7L,"put-out", null), new Tag(8L,"twin-tractor", null),
                        new Tag(9L,"endomysium", null), new Tag(10L,"murthering", null))).build();

        assertThrows(PersistenceException.class, ()->giftCertificateDao.create(toCreate));
    }

    @Test
    void testDeleteByCorrectId(){
        long deletionId = 1L;
        giftCertificateDao.delete(deletionId);

        assertTrue(giftCertificateDao.retrieveById(deletionId).isEmpty());
    }

    @Test
    void testDeleteByIncorrectId(){
        long deletionId = -12024L;

        assertThrows(IllegalArgumentException.class, ()->giftCertificateDao.delete(deletionId));
    }

    @Test
    void testUpdateCorrectParams(){
        GiftCertificate expected = GiftCertificate.builder()
                .id(2L).price(new BigDecimal("4400.86"))
                .duration(38)
                .createDate(LocalDateTime.from(format.parse("2022-04-14 16:56:02.000000")))
                .lastUpdateDate(LocalDateTime.from(format.parse("2022-04-13 15:45:23.000000")))
                .name("Walmart certificatee")
                .description("Lymantriidae shacklebone hematozzoa didactive glorification biochemically analabos anecdotist")
                .tags(Set.of(new Tag(2L,"subproofs", null)))
                .build();

        GiftCertificate toUpdate = GiftCertificate.builder()
                .id(2L)
                .name("Walmart certificate")
                .lastUpdateDate(LocalDateTime.parse("2022-04-13 15:45:23.000000",format))
                .duration(38)
                .tags(Set.of(new Tag(2L,"subproofs", null)))
                .build();

        toUpdate = giftCertificateDao.update(toUpdate);

        assertEquals(expected, toUpdate);
    }

    @Test
    void  testRetrieveByCriteriaOrderByDate(){
        List<GiftCertificate> expected = List.of(
                GiftCertificate.builder()
                        .id(2L).price(new BigDecimal("4400.86"))
                        .duration(56)
                        .createDate(LocalDateTime.from(format.parse("2022-04-14 16:56:02.000000")))
                        .lastUpdateDate(LocalDateTime.from(format.parse("2022-05-22 16:16:01.567089")))
                        .name("dimethylanthranilate Hentrich certificate")
                        .description("Lymantriidae shacklebone hematozzoa didactive glorification biochemically analabos anecdotist")
                        .tags(Set.of(new Tag(2L,"subproofs", null), new Tag(6L,"high-blazing", null))).build(),
                GiftCertificate.builder()
                        .id(1L).price(new BigDecimal("1690.83"))
                        .duration(11)
                        .createDate(LocalDateTime.from(format.parse("2022-03-08 15:33:15.000000")))
                        .lastUpdateDate(LocalDateTime.from(format.parse("2022-05-22 16:16:01.557016")))
                        .name("fanwise tornado-swept certificate")
                        .description("hooker-out Blase self-abhorring pseudotropine axemaster testing archdeceiver outsetting orientating")
                        .tags(Set.of(new Tag(2L,"subproofs", null), new Tag(6L,"high-blazing", null),
                                new Tag(7L,"put-out", null), new Tag(8L,"twin-tractor", null),
                                new Tag(9L,"endomysium", null), new Tag(10L,"murthering", null))).build());

        Criteria criteria = new Criteria();
        criteria.put(Criteria.ParamName.ORDER_DATE_DESC,"");

        List<GiftCertificate> actual = giftCertificateDao.retrieveByCriteria(criteria,1,10);

        assertEquals(expected, actual);
    }

    @Test
    void  testRetrieveByCriteriaOrderByName(){
        List<GiftCertificate> expected = List.of(
                GiftCertificate.builder()
                        .id(2L).price(new BigDecimal("4400.86"))
                        .duration(56)
                        .createDate(LocalDateTime.from(format.parse("2022-04-14 16:56:02.000000")))
                        .lastUpdateDate(LocalDateTime.from(format.parse("2022-05-22 16:16:01.567089")))
                        .name("dimethylanthranilate Hentrich certificate")
                        .description("Lymantriidae shacklebone hematozzoa didactive glorification biochemically analabos anecdotist")
                        .tags(Set.of(new Tag(2L,"subproofs", null), new Tag(6L,"high-blazing", null))).build(),
                GiftCertificate.builder()
                        .id(1L).price(new BigDecimal("1690.83"))
                        .duration(11)
                        .createDate(LocalDateTime.from(format.parse("2022-03-08 15:33:15.000000")))
                        .lastUpdateDate(LocalDateTime.from(format.parse("2022-05-22 16:16:01.557016")))
                        .name("fanwise tornado-swept certificate")
                        .description("hooker-out Blase self-abhorring pseudotropine axemaster testing archdeceiver outsetting orientating")
                        .tags(Set.of(new Tag(2L,"subproofs", null), new Tag(6L,"high-blazing", null),
                                new Tag(7L,"put-out", null), new Tag(8L,"twin-tractor", null),
                                new Tag(9L,"endomysium", null), new Tag(10L,"murthering", null))).build());

        Criteria criteria = new Criteria();
        criteria.put(Criteria.ParamName.ORDER_NAME_ASC,"");

        List<GiftCertificate> actual = giftCertificateDao.retrieveByCriteria(criteria,1,100);
        assertEquals(expected, actual);
    }

    @Test
    void  testRetrieveByCriteriaByTags(){
        List<GiftCertificate> expected = List.of(GiftCertificate.builder()
                .id(1L).price(new BigDecimal("1690.83"))
                .duration(11)
                .createDate(LocalDateTime.from(format.parse("2022-03-08 15:33:15.000000")))
                .lastUpdateDate(LocalDateTime.from(format.parse("2022-05-22 16:16:01.557016")))
                .name("fanwise tornado-swept certificate")
                .description("hooker-out Blase self-abhorring pseudotropine axemaster testing archdeceiver outsetting orientating")
                .tags(Set.of(new Tag(2L,"subproofs", null), new Tag(6L,"high-blazing", null),
                        new Tag(7L,"put-out", null), new Tag(8L,"twin-tractor", null),
                        new Tag(9L,"endomysium", null), new Tag(10L,"murthering", null))).build());

        Criteria criteria = new Criteria();
        criteria.put(Criteria.ParamName.TAG_NAMES, List.of("endomysium","murthering"));

        List<GiftCertificate> actual = giftCertificateDao.retrieveByCriteria(criteria,1,100);

        assertEquals(expected, actual);
    }
}
