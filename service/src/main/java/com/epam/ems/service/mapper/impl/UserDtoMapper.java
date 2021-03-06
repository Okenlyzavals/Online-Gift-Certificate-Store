package com.epam.ems.service.mapper.impl;

import com.epam.ems.dao.entity.User;
import com.epam.ems.dao.entity.role.Role;
import com.epam.ems.service.dto.UserDto;
import com.epam.ems.service.mapper.Mapper;
import org.springframework.stereotype.Component;


/**
 * Extension of {@link Mapper} suited for
 * {@link User} Entity and {@link UserDto} DTO.
 *
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
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
                entity.getPassword(),
                UserDto.Role.valueOf(entity.getRole().name()));
    }

    @Override
    public User extract(UserDto dto) {
        if(dto == null){return new User();}
        return new User(
                dto.getId(),
                dto.getUsername(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getRole()==null ? null : Role.valueOf(dto.getRole().name()),
                null);
    }
}
