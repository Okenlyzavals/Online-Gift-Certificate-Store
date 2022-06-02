package com.epam.ems.service.impl;

import com.epam.ems.dao.UserDao;
import com.epam.ems.dao.entity.User;
import com.epam.ems.service.UserService;
import com.epam.ems.service.dto.UserDto;
import com.epam.ems.service.exception.DuplicateEntityException;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserServiceImpl
        implements UserService, UserDetailsService {

    private final UserDao dao;
    private final Mapper<User, UserDto> mapper;

    @Autowired
    public UserServiceImpl(UserDao dao, Mapper<User, UserDto> mapper){
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public UserDto getById(Long id) throws NoSuchEntityException {
        return mapper.map(dao.findById(id)
                .orElseThrow(()->new NoSuchEntityException(User.class)));
    }

    @Override
    public Page<UserDto> getAll(int page, int elements) {
        Pageable request = PageRequest.of(page,elements, Sort.by(Sort.Direction.ASC, "id"));
        Page<User> result =  dao.findAll(request);
        return new PageImpl<>(
                result.stream().map(mapper::map).collect(Collectors.toList()),
                request,
                result.getTotalElements());
    }

    @Override
    public UserDto insert(UserDto entity) throws DuplicateEntityException {
        entity.setId(null);
        entity.setRole(UserDto.Role.USER);

        dao.findDistinctByUsername(entity.getUsername())
                .ifPresent(e->{throw new DuplicateEntityException(e.getId(),User.class);});
        return mapper.map(dao.save(mapper.extract(entity)));
    }

    @Override
    public void delete(Long id) throws NoSuchEntityException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(UserDto entity) throws NoSuchEntityException {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return mapper.map(dao.findDistinctByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("msg.error.not.found")));
    }
}
