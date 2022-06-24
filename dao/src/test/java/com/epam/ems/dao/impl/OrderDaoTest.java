package com.epam.ems.dao.impl;

import com.epam.ems.dao.GiftCertificateDao;
import com.epam.ems.dao.OrderDao;
import com.epam.ems.dao.UserDao;
import com.epam.ems.dao.config.TestDbConfig;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Order;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.dao.entity.User;
import com.epam.ems.dao.criteria.CriteriaQueryBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {TestDbConfig.class},
        loader = AnnotationConfigContextLoader.class)
@DataJpaTest
@EntityScan(basePackages = "com.epam.ems.*")
class OrderDaoTest {

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    @Autowired
    OrderDao dao;
    @Autowired
    UserDao userDao;
    @Autowired
    GiftCertificateDao giftCertificateDao;

    @Test
    void getAllTest(){
        List<Order> expected = List.of(
                Order.builder()
                        .id(1L)
                        .price(new BigDecimal("1690.83"))
                        .date(LocalDateTime.parse("2022-05-12 18:34:48.000000",format))
                        .user(User.builder()
                                .id(3L).username("Euploeinae")
                                .password("adiaphorism")
                                .email("Euploeinae@epam.test.com").build())
                        .certificates(Set.of(GiftCertificate.builder()
                                .id(1L).price(new BigDecimal("1690.83"))
                                .duration(11)
                                .createDate(LocalDateTime.from(format.parse("2022-03-08 15:33:15.000000")))
                                .lastUpdateDate(LocalDateTime.from(format.parse("2022-05-22 16:16:01.557016")))
                                .name("fanwise tornado-swept certificate")
                                .description("hooker-out Blase self-abhorring pseudotropine axemaster testing archdeceiver outsetting orientating")
                                .tags(Set.of(new Tag(2L,"subproofs", null), new Tag(6L,"high-blazing", null),
                                        new Tag(7L,"put-out", null), new Tag(8L,"twin-tractor", null),
                                        new Tag(9L,"endomysium", null), new Tag(10L,"murthering", null))).build()))
                        .build(),
                Order.builder()
                        .id(2L)
                        .price(new BigDecimal("6091.69"))
                        .date(LocalDateTime.parse("2022-03-18 14:27:48.000000",format))
                        .user(User.builder()
                                .id(1L).username("awakable")
                                .password("garmentworker")
                                .email("awakable@epam.test.com").build())
                        .certificates(Set.of(GiftCertificate.builder()
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
                                        .tags(Set.of(new Tag(2L,"subproofs", null), new Tag(6L,"high-blazing", null))).build()))
                        .build());

        List<Order> actual = dao.findAll(PageRequest.of(0,10)).toList();

        assertEquals(expected, actual);
    }

    @Test
    void  retrieveByIdTest(){
        Optional<Order> expected = Optional.of(Order.builder()
                .id(1L)
                .price(new BigDecimal("1690.83"))
                .date(LocalDateTime.parse("2022-05-12 18:34:48.000000",format))
                .user(User.builder()
                        .id(3L).username("Euploeinae")
                        .password("adiaphorism")
                        .email("Euploeinae@epam.test.com").build())
                .certificates(Set.of(GiftCertificate.builder()
                        .id(1L).price(new BigDecimal("1690.83"))
                        .duration(11)
                        .createDate(LocalDateTime.from(format.parse("2022-03-08 15:33:15.000000")))
                        .lastUpdateDate(LocalDateTime.from(format.parse("2022-05-22 16:16:01.557016")))
                        .name("fanwise tornado-swept certificate")
                        .description("hooker-out Blase self-abhorring pseudotropine axemaster testing archdeceiver outsetting orientating")
                        .tags(Set.of(new Tag(2L,"subproofs", null), new Tag(6L,"high-blazing", null),
                                new Tag(7L,"put-out", null), new Tag(8L,"twin-tractor", null),
                                new Tag(9L,"endomysium", null), new Tag(10L,"murthering", null))).build()))
                .build());

        Optional<Order> actual = dao.findById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void retrieveByIncorrectId(){
        long incorrectId = -12412141L;
        Optional<Order> actual = dao.findById(incorrectId);

        assertTrue(actual.isEmpty());
    }

    @Test
    void getByUserIdTest(){
        List<Order> expected = List.of(
                Order.builder()
                        .id(2L)
                        .price(new BigDecimal("6091.69"))
                        .date(LocalDateTime.parse("2022-03-18 14:27:48.000000",format))
                        .user(User.builder()
                                .id(1L).username("awakable")
                                .password("garmentworker")
                                .email("awakable@epam.test.com").build())
                        .certificates(Set.of(GiftCertificate.builder()
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
                                        .tags(Set.of(new Tag(2L,"subproofs", null), new Tag(6L,"high-blazing", null))).build()))
                        .build());

        List<Order> actual = dao.findAllByUserId(1L, PageRequest.of(0,10)).toList();

        assertEquals(expected, actual);
    }

    @Test
    void getByIncorrectUserIdTest(){
        long incorrectUserId = 3131L;
        List<Order> expected = List.of();

        List<Order> actual = dao.findAllByUserId(incorrectUserId, PageRequest.of(0,10)).toList();

        assertEquals(expected, actual);
    }

    @Test
    void deleteCorrectIdTest(){
        long correctId = 1L;

        dao.deleteById(correctId);
        Optional<Order> actual = dao.findById(correctId);

        assertTrue(actual.isEmpty());
    }

    @Test
    void  deleteByIncorrectId(){
        long incorrectId = -14141L;
        assertThrows(EmptyResultDataAccessException.class,()->dao.deleteById(incorrectId));
    }

    @Test
    void  testCreateNewOrder(){
        Order expected = Order.builder()
                .price(new BigDecimal("1690.83"))
                .date(LocalDateTime.parse("2022-05-22 12:34:48.000000",format))
                .user(userDao.findById(4L).get())
                .certificates(Set.of(giftCertificateDao.findById(2L).get()))
                .build();

        Order actual = dao.save(expected);
        expected.setId(3L);

        assertEquals(expected, actual);
    }
}
