package com.epam.ems.service;

import com.epam.ems.service.dto.OrderDto;
import com.epam.ems.service.dto.UserDto;
import com.epam.ems.service.exception.NoSuchEntityException;

import java.util.List;

public interface OrderService extends AbstractService<OrderDto> {

    List<OrderDto> getOrdersByUser(UserDto user) throws NoSuchEntityException;

}
