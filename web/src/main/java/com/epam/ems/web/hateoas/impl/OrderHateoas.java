package com.epam.ems.web.hateoas.impl;

import com.epam.ems.service.dto.OrderDto;
import com.epam.ems.web.controller.OrderController;
import com.epam.ems.web.hateoas.Hateoas;
import org.springframework.stereotype.Component;

import static com.epam.ems.web.hateoas.constant.HateoasConstant.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderHateoas implements Hateoas<OrderDto> {
    @Override
    public OrderDto buildHateoas(OrderDto model) {
        model.add(linkTo(methodOn(OrderController.class).get(model.getId())).withSelfRel());
        model.add(linkTo(methodOn(OrderController.class).getOrders(1,10)).withRel(ALL_ORDERS));
        model.add(linkTo(methodOn(OrderController.class)
                .getByUser(model.getUser().getId(),1,10))
                .withRel(ORDERS_BY_USER));
        model.add(linkTo(methodOn(OrderController.class)
                .makeOrder(model)).withRel(DUPLICATE));
        return model;
    }
}
