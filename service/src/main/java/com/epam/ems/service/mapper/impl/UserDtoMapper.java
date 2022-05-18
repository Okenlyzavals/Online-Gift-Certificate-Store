package com.epam.ems.service.mapper.impl;

import com.epam.ems.dao.entity.User;
import com.epam.ems.service.dto.UserDto;
import com.epam.ems.service.mapper.Mapper;
import org.springframework.stereotype.Component;


@Component
public class UserDtoMapper implements Mapper<User, UserDto> {

    @Override
    public UserDto map(User entity) {
        if(entity == null){
            return new UserDto();
        }

        return new UserDto(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword());
    }

    @Override
    public User extract(UserDto dto) {
        if(dto == null){
            return new User();
        }
        return new User(
                dto.getId(),
                dto.getUsername(),
                dto.getEmail(),
                dto.getPassword(),
                null);
    }
}
