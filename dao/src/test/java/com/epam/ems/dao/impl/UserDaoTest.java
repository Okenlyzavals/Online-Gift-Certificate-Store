package com.epam.ems.dao.impl;

import com.epam.ems.dao.UserDao;
import com.epam.ems.dao.config.TestDbConfig;
import com.epam.ems.dao.entity.User;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserDaoImpl.class,
        TestDbConfig.class},
        loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@Transactional
class UserDaoTest {

    @Autowired
    UserDao dao;

    @Test
    void getAllTest(){
        List<User> expected = List.of(
                User.builder()
                        .id(1L).username("awakable")
                        .password("garmentworker")
                        .email("awakable@epam.test.com").build(),
                User.builder()
                        .id(2L).username("buddles")
                        .password("preshows")
                        .email("buddles@epam.test.com").build(),
                User.builder()
                        .id(3L).username("Euploeinae")
                        .password("adiaphorism")
                        .email("Euploeinae@epam.test.com").build(),
                User.builder()
                        .id(4L).username("devocate")
                        .password("unhidden")
                        .email("devocate@epam.test.com").build());

        List<User> actual = dao.retrieveAll(1,10);

        assertEquals(expected, actual);
    }

    @Test
    void getByIdTest(){
        Optional<User> expected = Optional.of(User.builder()
                .id(4L).username("devocate")
                .password("unhidden")
                .email("devocate@epam.test.com").build());

        Optional<User> actual = dao.retrieveById(4L);

        assertEquals(expected, actual);
    }

    @Test
    void getByIncorrectIdTest(){
        long incorrectId = -1241L;

        Optional<User> actual = dao.retrieveById(incorrectId);

        assertTrue(actual.isEmpty());
    }


    @Test
    void testCreateUser(){
        User expected = User.builder()
                .username("testikov")
                .email("testikov66@mail.org")
                .password("lofasfasfaasfas")
                .build();

        User actual = dao.create(expected);
        expected.setId(5L);

        assertEquals(expected, actual);
    }

    @Test
    void testDeleteUser(){
        long toDeleteId = 1L;
        assertThrows(UnsupportedOperationException.class, ()->dao.delete(toDeleteId));
    }

}
