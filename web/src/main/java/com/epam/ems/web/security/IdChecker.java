package com.epam.ems.web.security;

import com.epam.ems.service.UserService;
import com.epam.ems.service.dto.UserDto;
import com.epam.ems.service.exception.NoSuchEntityException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class IdChecker {

    private final UserService service;

    public IdChecker(UserService service) {
        this.service = service;
    }

    public boolean checkUserId(Authentication authentication, Long id) {
        String name = authentication.getName();
        boolean res;
        try {
            UserDto result = service.getById(id);
            res = result.getUsername().equals(name);
        } catch (NoSuchEntityException e){
            res = false;
        }
        return res;
    }
}
