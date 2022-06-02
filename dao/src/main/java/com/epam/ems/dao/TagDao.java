package com.epam.ems.dao;

import com.epam.ems.dao.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagDao extends JpaRepository<Tag, Long> {

    /**
     * Retrieves tag from data source by its name.
     * @param name Name of tag to retrieve.
     * @return {@link Optional} of tag with this name.
     * If no tag was found, returns empty {@link Optional}
     */
    Optional<Tag> findDistinctByName(String name);

    /**
     * Retrieves the tag that is most widely used
     * by user that has the largest sum of all orders.
     * @return {@link Optional} of such tag.
     * If no tag was found, returns empty {@link Optional}
     */
    @Procedure(name = "Tag.GetMostUsedTag")
    Optional<Tag> findMostUsedTagOfUserWithHighestOrderCost();
}
