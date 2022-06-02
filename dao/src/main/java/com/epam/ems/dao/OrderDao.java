package com.epam.ems.dao;

import com.epam.ems.dao.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDao extends JpaRepository<Order,Long> {

    Page<Order> findAllByUserId(Long id, Pageable pageable);
}
