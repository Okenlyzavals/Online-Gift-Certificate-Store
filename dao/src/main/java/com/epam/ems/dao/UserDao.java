package com.epam.ems.dao;

import com.epam.ems.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repository for {@link User} entity.
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
@Repository
public interface UserDao extends JpaRepository<User,Long> {
    /**
     * Retrieves all orders made by user with such ID.
     * @param username Username of entity
     * @return {@link Optional} of User with this username.
     * If no user was found, returns empty {@link Optional}
     */
    Optional<User> findDistinctByUsername(String username);
}
