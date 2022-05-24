package com.epam.ems.web.hateoas.impl;

import com.epam.ems.service.dto.UserDto;
import com.epam.ems.web.controller.UserController;
import com.epam.ems.web.hateoas.Hateoas;
import org.springframework.stereotype.Component;

import static com.epam.ems.web.hateoas.constant.HateoasConstant.ALL_USERS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoas implements Hateoas<UserDto> {
    @Override
    public UserDto buildHateoas(UserDto model) {
        model.add(linkTo(methodOn(UserController.class).getUserById(model.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getUsers(1,10)).withRel(ALL_USERS));
        return model;
    }
}
