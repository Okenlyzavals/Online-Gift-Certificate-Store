package com.epam.ems.web.hateoas.impl;

import com.epam.ems.service.dto.TagDto;
import com.epam.ems.web.controller.TagController;
import com.epam.ems.web.hateoas.Hateoas;
import org.springframework.stereotype.Component;

import static com.epam.ems.web.hateoas.constant.HateoasConstant.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class TagHateoas implements Hateoas<TagDto> {

    @Override
    public TagDto buildHateoas(TagDto model) {
        model.add(linkTo(methodOn(TagController.class).getTagById(model.getId())).withSelfRel());
        model.add(linkTo(methodOn(TagController.class).getTags(1,10)).withRel(ALL_TAGS));
        model.add(linkTo(methodOn(TagController.class).getMostWidelyUsedTag())
                .withRel(WIDEST_TAG_OF_RICHEST_USER));
        model.add(linkTo(methodOn(TagController.class).deleteTag(model.getId()))
                .withRel(DELETE));
        return model;
    }

}
