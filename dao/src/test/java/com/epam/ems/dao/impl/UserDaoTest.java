package com.epam.ems.dao.impl;

import com.epam.ems.dao.UserDao;
import com.epam.ems.dao.config.TestDbConfig;
import com.epam.ems.dao.entity.User;
import com.epam.ems.dao.entity.role.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {TestDbConfig.class},
        loader = AnnotationConfigContextLoader.class)
@DataJpaTest
@EntityScan(basePackages = "com.epam.ems.*")
class UserDaoTest {

    @Autowired
    UserDao dao;

    @Test
    void getAllTest(){
        List<User> expected = List.of(
                User.builder()
                        .id(1L).username("awakable")
                        .password("garmentworker")
                        .email("awakable@epam.test.com")
                        .role(Role.USER).build(),
                User.builder()
                        .id(2L).username("buddles")
                        .password("preshows")
                        .email("buddles@epam.test.com")
                        .role(Role.USER).build(),
                User.builder()
                        .id(3L).username("Euploeinae")
                        .password("adiaphorism")
                        .email("Euploeinae@epam.test.com")
                        .role(Role.USER).build(),
                User.builder()
                        .id(4L).username("devocate")
                        .password("unhidden")
                        .email("devocate@epam.test.com")
                        .role(Role.ADMIN).build());

        List<User> actual = dao.findAll(PageRequest.of(0,10)).toList();

        assertEquals(expected, actual);
    }

    @Test
    void getByIdTest(){
        Optional<User> expected = Optional.of(User.builder()
                .id(4L).username("devocate")
                .password("unhidden")
                .email("devocate@epam.test.com")
                .role(Role.ADMIN).build());

        Optional<User> actual = dao.findById(4L);

        assertEquals(expected, actual);
    }

    @Test
    void getByIncorrectIdTest(){
        long incorrectId = -1241L;

        Optional<User> actual = dao.findById(incorrectId);

        assertTrue(actual.isEmpty());
    }


    @Test
    void testCreateUser(){
        User expected = User.builder()
                .username("testikov")
                .email("testikov66@mail.org")
                .password("lofasfasfaasfas")
                .role(Role.USER)
                .build();

        User actual = dao.save(expected);
        expected.setId(5L);

        assertEquals(expected, actual);
    }

    @Test
    void testDeleteUser(){
        long toDeleteId = 1L;
        assertDoesNotThrow(()->dao.deleteById(toDeleteId));
    }

    @Test
    void testFindByCorrectUsername(){
        String username = "devocate";
        Optional<User> expected = Optional.of(User.builder()
                .id(4L).username(username)
                .password("unhidden")
                .email("devocate@epam.test.com")
                .role(Role.ADMIN).build());

        Optional<User> actual = dao.findDistinctByUsername(username);

        assertEquals(expected, actual);
    }

    @Test
    void testFindByIncorrectUsername(){
        String username = "iammissing";
        Optional<User> expected = Optional.empty();

        Optional<User> actual = dao.findDistinctByUsername(username);

        assertEquals(expected, actual);
    }

}
