package com.epam.ems.service.impl;

import com.epam.ems.dao.UserDao;
import com.epam.ems.dao.entity.User;
import com.epam.ems.service.UserService;
import com.epam.ems.service.dto.UserDto;
import com.epam.ems.service.exception.DuplicateEntityException;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@ComponentScan({"com.epam.ems.dao", "com.epam.ems.service"})
public class UserServiceImpl implements UserService {

    private final UserDao dao;
    private final Mapper<User, UserDto> mapper;

    @Autowired
    public UserServiceImpl(UserDao dao, Mapper<User, UserDto> mapper){
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public UserDto getById(Long id) throws NoSuchEntityException {
        return mapper.map(dao.retrieveById(id)
                .orElseThrow(()->new NoSuchEntityException(User.class)));
    }

    @Override
    public List<UserDto> getAll(int page, int elements) {
        return dao.retrieveAll(page,elements)
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto insert(UserDto entity) throws DuplicateEntityException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long id) throws NoSuchEntityException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(UserDto entity) throws NoSuchEntityException {
        throw new UnsupportedOperationException();
    }
}
