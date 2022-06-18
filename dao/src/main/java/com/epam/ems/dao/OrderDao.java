package com.epam.ems.dao;

import com.epam.ems.dao.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository for {@link Order} entity.
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
@Repository
public interface OrderDao extends JpaRepository<Order,Long> {

    /**
     * Retrieves all orders made by user with such ID.
     * @param id    ID of a user
     * @param pageable Pagination information.
     * @return  A Page of Order entities.
     */
    Page<Order> findAllByUserId(Long id, Pageable pageable);
}
