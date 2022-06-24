package com.epam.ems.web.hateoas;

import com.epam.ems.service.dto.OrderDto;
import com.epam.ems.web.controller.OrderController;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static com.epam.ems.web.hateoas.constant.HateoasConstant.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<OrderDto, OrderDto> {
    @Override
    public OrderDto toModel(OrderDto entity) {
        return entity.add(
                linkTo(methodOn(OrderController.class).get(entity.getId())).withSelfRel(),
                linkTo(OrderController.class).withRel(ALL_ORDERS),
                linkTo(OrderController.class).slash(USER).slash(entity.getUser().getId())
                        .withRel(ORDERS_BY_USER),
                linkTo(methodOn(OrderController.class).makeOrder(entity)).withRel(DUPLICATE));
    }
}
