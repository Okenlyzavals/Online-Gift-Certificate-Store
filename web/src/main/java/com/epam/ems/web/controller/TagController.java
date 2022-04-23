package com.epam.ems.web.controller;

import com.epam.ems.service.TagService;
import com.epam.ems.service.dto.GiftCertificateDto;
import com.epam.ems.service.dto.TagDto;
import com.epam.ems.service.exception.DuplicateEntityException;
import com.epam.ems.service.exception.NoSuchEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * API class for basic operations with tags.
 * @author Baranouski Y. K.
 * @version 1.0.0
 */
@RestController
@Validated
@RequestMapping(value = "/tags")
@ComponentScan({"com.epam.ems.service.impl", "com.epam.ems.web.exception"})
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    /**
     * Returns all tags stored in data source
     * @return List of {@link TagDto} retrieved from data source
     */
    @GetMapping
    public List<TagDto> getTags(){
        return tagService.getAll();
    }

    /**
     * Returns tag stored in data source under specific ID.
     * @param id ID of a tag
     * @return Instance of {@link TagDto} retrieved from data source
     * @throws NoSuchEntityException if none was found
     */
    @GetMapping("/{id}")
    public TagDto getTagById(
            @PathVariable("id") @Min(value = 1, message = "msg.id.negative") long id) throws NoSuchEntityException {
        return tagService.getById(id);
    }

    /**
     * Creates new tag in data source
     * @param tagDto {@link TagDto} to create
     * @throws DuplicateEntityException if the same tag already exists
     */
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto createTag(
            @NotNull(message = "msg.dto.null") @Valid @RequestBody  TagDto tagDto) throws DuplicateEntityException {
        return tagService.insert(tagDto);
    }

    /**
     * Removes tag from Data Source by its ID.
     * @param id ID of tag to delete.
     * @throws NoSuchEntityException if such tag does not exist in data source.
     */
    @DeleteMapping("/{id}")
    public void deleteTag(
            @PathVariable("id") @Min(value = 1, message = "msg.id.negative") long id) throws NoSuchEntityException{
        tagService.delete(id);
    }

}
