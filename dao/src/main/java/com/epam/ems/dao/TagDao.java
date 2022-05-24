package com.epam.ems.dao;

import com.epam.ems.dao.entity.Tag;
import java.util.Optional;

/**
 * Extension of {@link AbstractDao} suited for {@link Tag} entities.
 *
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
public interface TagDao extends AbstractDao<Tag>{

    /**
     * Retrieves tag from data source by its name.
     * @param name Name of tag to retrieve.
     * @return {@link Optional} of tag with this name.
     * If no tag was found, returns empty {@link Optional}
     */
    Optional<Tag> findByName(String name);

    /**
     * Retrieves the tag that is most widely used
     * by user that has the largest sum of all orders.
     * @return {@link Optional} of such tag.
     * If no tag was found, returns empty {@link Optional}
     */
    Optional<Tag> findMostUsedTagOfUserWithHighestOrderCost();
}
