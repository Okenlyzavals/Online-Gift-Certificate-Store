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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
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
        return mapper.map(dao.findById(id).orElseThrow(()->new NoSuchEntityException(Order.class)));
    }

    @Override
    public Page<OrderDto> getAll(int page, int elements) {
        Pageable request = PageRequest.of(page,elements, Sort.by(Sort.Direction.ASC, "id"));
        Page<Order> result =  dao.findAll(request);
        return new PageImpl<>(
                result.stream().map(mapper::map).collect(Collectors.toList()),
                request,
                result.getTotalElements());
    }

    @Override
    public OrderDto insert(OrderDto entity) throws DuplicateEntityException {
        Order order = mapper.extract(entity);
        order.setId(null);
        order.setCertificates(getCertificatesPreparedForDbOperations(order));
        order.setUser(getUserForDbOperation(order));
        order.setPrice(
                order.getCertificates().stream()
                        .map(GiftCertificate::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        return mapper.map(dao.save(order));
    }

    @Override
    public void delete(Long id) throws NoSuchEntityException {
        dao.findById(id).ifPresentOrElse(
                e->dao.deleteById(id),
                ()-> {throw new NoSuchEntityException(Order.class);});
    }

    @Override
    public void delete(OrderDto entity) throws NoSuchEntityException {
        delete(entity.getId());
    }

    @Override
    public Page<OrderDto> getOrdersByUser(Long id, int page, int elements) throws NoSuchEntityException {

        User user =  userDao.findById(id).orElseThrow(()->new NoSuchEntityException(User.class));

        Pageable request = PageRequest.of(page,elements, Sort.by(Sort.Direction.ASC, "id"));
        Page<Order> result =  dao.findAllByUserId(user.getId(), request);
        return new PageImpl<>(
                result.stream().map(mapper::map).collect(Collectors.toList()),
                request,
                result.getTotalElements());
    }

    private Set<GiftCertificate> getCertificatesPreparedForDbOperations(Order order){
        Set<GiftCertificate> preparedCerts = new HashSet<>();
        for (GiftCertificate certificate : order.getCertificates()){
            certDao.findById(certificate.getId()).ifPresent(preparedCerts::add);
        }
        return preparedCerts;
    }

    private User getUserForDbOperation(Order order){
        return userDao.findById(order.getUser().getId())
                .orElseThrow(()->new NoSuchEntityException(User.class));
    }
}
