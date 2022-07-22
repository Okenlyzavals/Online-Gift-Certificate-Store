package com.epam.ems.web.controller;

import com.epam.ems.service.TagService;
import com.epam.ems.service.dto.TagDto;
import com.epam.ems.service.exception.DuplicateEntityException;
import com.epam.ems.service.exception.NoSuchEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * API class for basic operations with tags.
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
@RestController
@Validated
@RequestMapping(value = "/tags")
public class TagController {

    private final TagService tagService;
    private final RepresentationModelAssembler<TagDto, TagDto> hateoas;

    @Autowired
    public TagController(TagService tagService, RepresentationModelAssembler<TagDto, TagDto> hateoas){
        this.tagService = tagService;
        this.hateoas = hateoas;
    }

    @GetMapping
    public PagedModel<TagDto> getTags(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "5") @Min(1) int size,
            PagedResourcesAssembler<TagDto> assembler) {

        Page<TagDto> res = tagService.getAll(page, size);
        return assembler.toModel(res, hateoas);
    }

    @GetMapping("/{id}")
    public TagDto getTagById(
            @PathVariable("id") @Min(value = 1, message = "msg.id.negative") long id)
            throws NoSuchEntityException {
        return hateoas.toModel(tagService.getById(id));
    }

    @GetMapping("/prevalent")
    public TagDto getMostWidelyUsedTag(){
        return hateoas.toModel(tagService.retrieveMostUsedTagOfUserWithLargestOrderCost());
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto createTag(
            @NotNull(message = "msg.dto.null") @Valid @RequestBody  TagDto tagDto) throws DuplicateEntityException {
        return hateoas.toModel(tagService.insert(tagDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTag(
            @PathVariable("id") @Min(value = 1, message = "msg.id.negative") long id) throws NoSuchEntityException{
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
