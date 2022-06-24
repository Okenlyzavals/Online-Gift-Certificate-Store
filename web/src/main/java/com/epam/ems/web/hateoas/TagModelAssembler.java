package com.epam.ems.web.hateoas;

import com.epam.ems.service.dto.TagDto;
import com.epam.ems.web.controller.TagController;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static com.epam.ems.web.hateoas.constant.HateoasConstant.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelAssembler implements RepresentationModelAssembler<TagDto, TagDto> {

    @Override
    public TagDto toModel(TagDto entity) {
        return entity.add(
                linkTo(methodOn(TagController.class).getTagById(entity.getId())).withSelfRel(),
                linkTo(TagController.class).withRel(ALL_TAGS),
                linkTo(methodOn(TagController.class).getMostWidelyUsedTag()).withRel(WIDEST_TAG_OF_RICHEST_USER),
                linkTo(methodOn(TagController.class).deleteTag(entity.getId())).withRel(DELETE));

    }
}
