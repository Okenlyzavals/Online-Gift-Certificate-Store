package com.epam.ems.service.impl;

import com.epam.ems.dao.UserDao;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.dao.entity.User;
import com.epam.ems.dao.entity.role.Role;
import com.epam.ems.service.dto.UserDto;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.mapper.impl.UserDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Spy
    UserDtoMapper mapper = new UserDtoMapper();
    @Mock
    UserDao userDao;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void testGetAll(){
        List<User> userlist = List.of(new User(1L,"user1","user1@mail.com","pass",Role.USER,null),
                new User(2L,"user2","user2@mail.com","pass", Role.USER, null),
                new User(3L,"user3","user3@mail.com","pass",Role.USER,null));
        List<UserDto> expected = userlist.stream().map(mapper::map).collect(Collectors.toList());
        when(userDao.findAll((Pageable) any())).thenReturn(new PageImpl<>(userlist));

        List<UserDto> actual = service.getAll(1,100).toList();
        assertEquals(expected, actual);
    }

    @Test
    void testGetByCorrectId(){
        User user = new User(1L,"user1","user1@mail.com","pass",Role.USER,null);
        UserDto expected = mapper.map(user);
        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        UserDto actual = service.getById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void testGetByIncorrectId(){
        when(userDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityException.class, ()->service.getById(1L));
    }

    @Test
    void testInsert(){
        UserDto dto = new UserDto();
        assertDoesNotThrow(()->service.insert(dto));
    }

    @Test
    void testDeleteById(){
        assertThrows(UnsupportedOperationException.class, ()->service.delete(2L));
    }

    @Test
    void testDeleteEntity(){
        UserDto dto = new UserDto();
        assertThrows(UnsupportedOperationException.class, ()->service.delete(dto));
    }

    @Test
    void testFindByUsername(){
        User user = new User(1L,"user1","user1@mail.com","pass",Role.USER,null);
        UserDto expected = mapper.map(user);
        when(userDao.findDistinctByUsername(anyString())).thenReturn(Optional.of(user));

        UserDetails actual = service.loadUserByUsername("user1");

        assertEquals(expected, actual);
    }
}
