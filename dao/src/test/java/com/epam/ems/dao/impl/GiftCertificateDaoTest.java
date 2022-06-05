package com.epam.ems.dao.impl;

import com.epam.ems.dao.GiftCertificateDao;
import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.config.TestDbConfig;
import com.epam.ems.dao.criteria.CriteriaQueryForCount;
import com.epam.ems.dao.criteria.CriteriaQueryForEntities;
import com.epam.ems.dao.criteria.parser.CertificateCriteriaQueryParameterParser;
import com.epam.ems.dao.criteria.parser.OrderParser;
import com.epam.ems.dao.criteria.parser.PredicateParser;
import com.epam.ems.dao.custom.GiftCertificateDaoCustom;
import com.epam.ems.dao.custom.GiftCertificateDaoImpl;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.dao.entity.criteria.CertificateCriteria;
import com.epam.ems.dao.criteria.CriteriaQueryBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@ContextConfiguration(
        classes = {TestDbConfig.class},
        loader = AnnotationConfigContextLoader.class)
@DataJpaTest
@EntityScan(basePackages = "com.epam.ems.*")
class GiftCertificateDaoTest {

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

        List<GiftCertificate> actual = giftCertificateDao.findAll(PageRequest.of(0,10)).toList();

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
        Optional<GiftCertificate> actual = giftCertificateDao.findById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void testGetByNonExistingId(){
        Optional<GiftCertificate> expected = Optional.empty();
        Optional<GiftCertificate> actual = giftCertificateDao.findById(-14124124L);

        assertEquals(expected, actual);
    }

    @Test
    void testCreateNewCertificate(){
        Set<Tag> tags = new HashSet<>(Set.of(tagDao.findById(2L).get(),
                tagDao.findById(1L).get(),
                tagDao.findById(8L).get()));
        GiftCertificate toCreate =  GiftCertificate.builder()
                .price(new BigDecimal("1000.0"))
                .duration(14)
                .createDate(LocalDateTime.from(format.parse("2022-03-08 15:33:15.000000")))
                .lastUpdateDate(LocalDateTime.from(format.parse("2022-05-22 16:16:01.557016")))
                .name("throw me some numbers certificate")
                .description("fits everyone")
                .tags(tags).orders(null).build();

        GiftCertificate actual = giftCertificateDao.save(toCreate);
        toCreate.setId(3L);

        assertEquals(toCreate, actual);
    }

    @Test
    void testDeleteByCorrectId(){
        long deletionId = 1L;
        giftCertificateDao.deleteById(deletionId);

        assertTrue(giftCertificateDao.findById(deletionId).isEmpty());
    }

    @Test
    void testDeleteByIncorrectId(){
        long deletionId = -12024L;

        assertThrows(EmptyResultDataAccessException.class, ()->giftCertificateDao.deleteById(deletionId));
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

        toUpdate = giftCertificateDao.save(toUpdate);

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

        CertificateCriteria criteria = new CertificateCriteria();
        criteria.put(CertificateCriteria.ParamName.ORDER_DATE_DESC,"");

        List<GiftCertificate> actual = giftCertificateDao.retrieveByCriteria(criteria,PageRequest.of(0,10)).toList();

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

        CertificateCriteria criteria = new CertificateCriteria();
        criteria.put(CertificateCriteria.ParamName.ORDER_NAME_ASC,"");

        List<GiftCertificate> actual = giftCertificateDao.retrieveByCriteria(criteria,PageRequest.of(0,100)).toList();
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

        CertificateCriteria criteria = new CertificateCriteria();
        criteria.put(CertificateCriteria.ParamName.TAG_NAMES, List.of("endomysium","murthering"));

        List<GiftCertificate> actual = giftCertificateDao.retrieveByCriteria(criteria,PageRequest.of(0,10)).toList();

        assertEquals(expected, actual);
    }
}
