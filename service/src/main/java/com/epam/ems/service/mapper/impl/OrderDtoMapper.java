package com.epam.ems.service.mapper.impl;

import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Order;
import com.epam.ems.dao.entity.User;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.dto.OrderDto;
import com.epam.ems.service.dto.UserDto;
import com.epam.ems.service.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderDtoMapper implements Mapper<Order, OrderDto> {

    private final Mapper<User,UserDto> userMapper;
    private final Mapper<GiftCertificate, GiftCertificateDto> certMapper;

    @Autowired
    public OrderDtoMapper(Mapper<User, UserDto> userMapper, Mapper<GiftCertificate, GiftCertificateDto> certMapper) {
        this.userMapper = userMapper;
        this.certMapper = certMapper;
    }

    @Override
    public OrderDto map(Order entity) {
        if(entity == null){
            return new OrderDto();
        }
        return new OrderDto(
                entity.getId(),
                entity.getPrice(),
                entity.getDate(),
                userMapper.map(entity.getUser()),
                entity.getCertificates().stream()
                        .distinct()
                        .map(certMapper::map)
                        .collect(Collectors.toList()));
    }

    @Override
    public Order extract(OrderDto dto) {
        return new Order(
                dto.getId(),
                dto.getPrice(),
                dto.getDate(),
                userMapper.extract(dto.getUser()),
                dto.getCertificates().stream().map(certMapper::extract)
                        .collect(Collectors.toSet()));
    }
}
