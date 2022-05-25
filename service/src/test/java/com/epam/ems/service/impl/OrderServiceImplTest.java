package com.epam.ems.service.impl;

import com.epam.ems.dao.GiftCertificateDao;
import com.epam.ems.dao.OrderDao;
import com.epam.ems.dao.UserDao;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Order;
import com.epam.ems.dao.entity.User;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.dto.OrderDto;
import com.epam.ems.service.dto.UserDto;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.mapper.impl.GiftCertificateDtoMapper;
import com.epam.ems.service.mapper.impl.OrderDtoMapper;
import com.epam.ems.service.mapper.impl.TagDtoMapper;
import com.epam.ems.service.mapper.impl.UserDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Spy
    OrderDtoMapper mapper = new OrderDtoMapper(new UserDtoMapper(), new GiftCertificateDtoMapper(new TagDtoMapper()));
    @Mock
    UserDao userDao;
    @Mock
    GiftCertificateDao giftCertificateDao;
    @Mock
    OrderDao orderDao;

    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    void getAllTest(){
        List<Order> orders = List.of(Order.builder()
                .id(1L).date(LocalDateTime.MIN).price(new BigDecimal(1000))
                        .user(User.builder().id(1L).build())
                        .certificates(Set.of(GiftCertificate.builder().id(1L).build(),
                                GiftCertificate.builder().id(2L).build(),
                                GiftCertificate.builder().id(3L).build()))
                .build(),
                Order.builder()
                        .id(2L).date(LocalDateTime.MIN).price(new BigDecimal(1500))
                        .user(User.builder().id(2L).build())
                        .certificates(Set.of(GiftCertificate.builder().id(4L).build(),
                                GiftCertificate.builder().id(2L).build(),
                                GiftCertificate.builder().id(3L).build()))
                        .build());
        List<OrderDto> expected = orders.stream().map(mapper::map).collect(Collectors.toList());
        when(orderDao.retrieveAll(anyInt(), anyInt())).thenReturn(orders);

        List<OrderDto> actual = orderService.getAll(1,100);

        assertEquals(expected, actual);
    }

    @Test
    void getByCorrectIdTest(){
        Optional<Order> order = Optional.of(Order.builder()
                .id(1L).date(LocalDateTime.MIN).price(new BigDecimal(1000))
                .user(User.builder().id(1L).build())
                .certificates(Set.of(
                        GiftCertificate.builder().id(1L).build(),
                        GiftCertificate.builder().id(2L).build(),
                        GiftCertificate.builder().id(3L).build()))
                .build());
        OrderDto expected = mapper.map(order.get());
        when(orderDao.retrieveById(anyLong())).thenReturn(order);

        OrderDto actual = orderService.getById(1L);

        assertEquals(expected,actual);
    }

    @Test
    void getByIncorrectIdTest(){
        when(orderDao.retrieveById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityException.class, ()->orderService.getById(1L));
    }

    @Test
    void getByUserTest(){
        List<Order> orders = List.of(Order.builder()
                        .id(1L).date(LocalDateTime.MIN).price(new BigDecimal(1000))
                        .user(User.builder().id(1L).build())
                        .certificates(Set.of(GiftCertificate.builder().id(1L).build(),
                                GiftCertificate.builder().id(2L).build(),
                                GiftCertificate.builder().id(3L).build()))
                        .build(),
                Order.builder()
                        .id(1L).date(LocalDateTime.MIN).price(new BigDecimal(1500))
                        .user(User.builder().id(2L).build())
                        .certificates(Set.of(GiftCertificate.builder().id(4L).build(),
                                GiftCertificate.builder().id(2L).build(),
                                GiftCertificate.builder().id(3L).build()))
                        .build());
        List<OrderDto> expected = orders.stream().map(mapper::map).collect(Collectors.toList());
        when(userDao.retrieveById(anyLong())).thenReturn(Optional.of(User.builder().build()));
        when(orderDao.retrieveByUserId(eq(1L),anyInt(),anyInt())).thenReturn(orders);

        List<OrderDto> actual = orderService.getOrdersByUser(1L,1,100);

        assertEquals(expected, actual);
    }

    @Test
    void getByIncorrectUserTest(){
        when(userDao.retrieveById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityException.class, ()->orderService.getOrdersByUser(1L,1,100));
    }

    @Test
    void insertNewTest(){
        OrderDto toInsert = new OrderDto(1L,new BigDecimal(1000),LocalDateTime.MIN,
                new UserDto(1L,null,null,null),
                List.of(new GiftCertificateDto(1L,null,null,null,null,null,null,null),
                        new GiftCertificateDto(2L,null,null,null,null,null,null,null),
                        new GiftCertificateDto(3L,null,null,null,null,null,null,null)));
        when(userDao.retrieveById(anyLong())).thenReturn(Optional.of(User.builder().build()));
        when(giftCertificateDao.retrieveById(anyLong()))
                .thenReturn(Optional.of(GiftCertificate.builder().build()));
        when(orderDao.create(any())).thenReturn(Order.builder().certificates(Set.of()).build());

        assertDoesNotThrow(()->orderService.insert(toInsert));
    }

    @Test
    void deleteExistingByIdTest(){
        when(orderDao.retrieveById(anyLong())).thenReturn(Optional.of(Order.builder().build()));
        assertDoesNotThrow(()->orderService.delete(1L));
    }

    @Test
    void deleteNonexistentByIdTest(){
        when(orderDao.retrieveById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, ()->orderService.delete(1L));
    }
}
