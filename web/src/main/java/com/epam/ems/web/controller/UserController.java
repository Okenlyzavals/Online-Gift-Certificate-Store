package com.epam.ems.web.controller;

import com.epam.ems.service.UserService;
import com.epam.ems.service.dto.UserDto;
import com.epam.ems.web.hateoas.Hateoas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/users")
@ComponentScan({"com.epam.ems.service.impl", "com.epam.ems.web.exception"})
public class UserController {

    private final UserService service;
    private final Hateoas<UserDto> hateoas;

    @Autowired
    public UserController(UserService service, Hateoas<UserDto> hateoas){
        this.service = service;
        this.hateoas = hateoas;
    }

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "5") @Min(1)int elements){
        List<UserDto> users = service.getAll(page, elements);
        users.forEach(hateoas::buildHateoas);
        return users;
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable @Min(1) long id){
        return hateoas.buildHateoas(service.getById(id));
    }

}
