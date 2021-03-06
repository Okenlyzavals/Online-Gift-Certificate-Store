package com.epam.ems.service;

import com.epam.ems.service.dto.TagDto;
import com.epam.ems.service.exception.NoSuchEntityException;

/**
 * Extension of {@link AbstractService} suited for work with {@link TagDto}
 *
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
public interface TagService extends AbstractService<TagDto> {

    /**
     * Retrieves tag from Data Source by its name.
     * @param name Name of tag to retrieve.
     * @return Instance of {@link TagDto} with this name.
     * @throws NoSuchEntityException if no tag with this name was found.
     */
    TagDto getByName(String name) throws NoSuchEntityException;

    /**
     * Retrieves the tag that is most widely used
     * by user that has the largest sum of all orders.
     * @return instance of {@link TagDto}
     * @throws NoSuchEntityException if no tag fitting such demand was found.
     */
    TagDto retrieveMostUsedTagOfUserWithLargestOrderCost() throws NoSuchEntityException;
}
