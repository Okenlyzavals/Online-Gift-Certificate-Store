package com.epam.ems.web.controller;

import com.epam.ems.service.UserService;
import com.epam.ems.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final RepresentationModelAssembler<UserDto,UserDto> hateoas;

    @Autowired
    public UserController(UserService service, RepresentationModelAssembler<UserDto,UserDto> hateoas){
        this.service = service;
        this.hateoas = hateoas;
    }

    @GetMapping
    public PagedModel<UserDto> getUsers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "5") @Min(1) int size,
            PagedResourcesAssembler<UserDto> pagedResourcesAssembler){
        Page<UserDto> users = service.getAll(page, size);
        return pagedResourcesAssembler.toModel(users, hateoas);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable @Min(1) long id){
        return hateoas.toModel(service.getById(id));
    }

}
