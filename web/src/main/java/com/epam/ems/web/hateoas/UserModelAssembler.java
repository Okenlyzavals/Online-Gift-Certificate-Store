package com.epam.ems.web.hateoas;

import com.epam.ems.service.dto.UserDto;
import com.epam.ems.web.controller.OrderController;
import com.epam.ems.web.controller.UserController;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static com.epam.ems.web.hateoas.constant.HateoasConstant.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDto,UserDto> {
    @Override
    public UserDto toModel(UserDto entity) {
        return entity.add(
                linkTo(methodOn(UserController.class).getUserById(entity.getId())).withSelfRel(),
                linkTo(UserController.class).withRel(ALL_USERS),
                linkTo(OrderController.class).slash(USER).withRel(ORDERS_BY_USER));
    }
}
