package com.epam.ems.service.impl;

import com.epam.ems.dao.GiftCertificateDao;
import com.epam.ems.dao.OrderDao;
import com.epam.ems.dao.UserDao;
import com.epam.ems.dao.entity.GiftCertificate;
import com.epam.ems.dao.entity.Order;
import com.epam.ems.dao.entity.User;
import com.epam.ems.service.OrderService;
import com.epam.ems.service.dto.OrderDto;
import com.epam.ems.service.exception.DuplicateEntityException;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.service.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderDao dao;
    private final UserDao userDao;
    private final GiftCertificateDao certDao;
    private final Mapper<Order, OrderDto> mapper;

    @Autowired
    public OrderServiceImpl(OrderDao dao, UserDao userDao,
                            GiftCertificateDao certDao, Mapper<Order, OrderDto> mapper){
        this.dao = dao;
        this.userDao = userDao;
        this.certDao = certDao;
        this.mapper = mapper;
    }

    @Override
    public OrderDto getById(Long id) throws NoSuchEntityException {
        return mapper.map(dao.retrieveById(id).orElseThrow(()->new NoSuchEntityException(Order.class)));
    }

    @Override
    public List<OrderDto> getAll(int page, int elements) {
        return dao.retrieveAll(page,elements)
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto insert(OrderDto entity) throws DuplicateEntityException {
        Order order = mapper.extract(entity);
        order.setId(null);
        order.setCertificates(getCertificatesPreparedForDbOperations(order));
        order.setUser(getUserForDbOperation(order));
        return mapper.map(dao.create(order));
    }

    @Override
    public void delete(Long id) throws NoSuchEntityException {
        dao.retrieveById(id).ifPresentOrElse(
                e->dao.delete(id),
                ()-> {throw new NoSuchEntityException(Order.class);});
    }

    @Override
    public void delete(OrderDto entity) throws NoSuchEntityException {
        delete(entity.getId());
    }

    @Override
    public List<OrderDto> getOrdersByUser(Long id, int page, int elements) throws NoSuchEntityException {

        User user =  userDao.retrieveById(id).orElseThrow(()->new NoSuchEntityException(User.class));
        return dao.retrieveByUserId(
                user.getId(),
                page,
                elements).stream().map(mapper::map).collect(Collectors.toList());
    }

    private Set<GiftCertificate> getCertificatesPreparedForDbOperations(Order order){
        Set<GiftCertificate> preparedCerts = new HashSet<>();
        for (GiftCertificate certificate : order.getCertificates()){
            certDao.retrieveById(certificate.getId()).ifPresent(preparedCerts::add);
        }
        return preparedCerts;
    }

    private User getUserForDbOperation(Order order){
        return userDao.retrieveById(order.getUser().getId())
                .orElseThrow(()->new NoSuchEntityException(User.class));
    }
}
