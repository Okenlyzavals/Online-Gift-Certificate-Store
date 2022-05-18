package com.epam.ems.web.hateoas;

import com.epam.ems.service.dto.UserDto;
import com.epam.ems.web.controller.TagController;
import com.epam.ems.web.controller.UserController;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoas implements Hateoas<UserDto> {
    @Override
    public UserDto buildHateoas(UserDto model) {
        model.add(linkTo(methodOn(UserController.class).get(model.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getUsers(1,10)).withRel("allUsers"));
        return model;
    }
}
