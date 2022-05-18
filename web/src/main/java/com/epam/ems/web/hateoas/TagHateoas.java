package com.epam.ems.web.hateoas;

import com.epam.ems.service.dto.TagDto;
import com.epam.ems.web.controller.TagController;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class TagHateoas implements Hateoas<TagDto>{

    @Override
    public TagDto buildHateoas(TagDto model) {
        model.add(linkTo(methodOn(TagController.class).getTagById(model.getId())).withSelfRel());
        model.add(linkTo(methodOn(TagController.class).getTags(1,10)).withRel("allTags"));
        model.add(linkTo(methodOn(TagController.class).getMostWidelyUsedTag())
                .withRel("mostUsedTagOfUserWithLargestOrderCost"));
        model.add(linkTo(methodOn(TagController.class).deleteTag(model.getId()))
                .withRel("delete"));
        return model;
    }

}
