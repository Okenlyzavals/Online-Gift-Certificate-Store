package com.epam.ems.web.hateoas;

import com.epam.ems.service.dto.OrderDto;
import com.epam.ems.web.controller.OrderController;
import com.epam.ems.web.controller.TagController;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderHateoas implements Hateoas<OrderDto> {
    @Override
    public OrderDto buildHateoas(OrderDto model) {
        model.add(linkTo(methodOn(OrderController.class).get(model.getId())).withSelfRel());
        model.add(linkTo(methodOn(OrderController.class).getOrders(1,10)).withRel("allTags"));
        model.add(linkTo(methodOn(OrderController.class)
                .getByUser(model.getUser().getId(), model.getUser()))
                .withRel("byUser"));
        model.add(linkTo(methodOn(OrderController.class)
                .makeOrder(model)).withRel("duplicateOrder"));
        return model;
    }
}
