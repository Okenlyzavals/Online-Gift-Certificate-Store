package com.epam.ems.web.controller;

import com.epam.ems.service.UserService;
import com.epam.ems.service.dto.UserDto;
import com.epam.ems.web.hateoas.Hateoas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final Hateoas<UserDto> hateoas;

    @Autowired
    public UserController(UserService service, Hateoas<UserDto> hateoas){
        this.service = service;
        this.hateoas = hateoas;
    }

    @GetMapping
    public CollectionModel<UserDto> getUsers(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "5") @Min(1)int elements){
        List<UserDto> users = service.getAll(page, elements);
        users.forEach(hateoas::buildHateoas);
        return hateoas.buildPaginationModel(users,
                ()->page < 2 ? null : methodOn(getClass()).getUsers(1,elements),
                ()->users.size() < elements ? null : methodOn(getClass()).getUsers(page+1, elements),
                ()->page < 2 ? null : methodOn(getClass()).getUsers(page-1, elements));
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable @Min(1) long id){
        return hateoas.buildHateoas(service.getById(id));
    }

}
