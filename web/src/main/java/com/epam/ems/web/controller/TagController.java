package com.epam.ems.web.controller;

import com.epam.ems.service.TagService;
import com.epam.ems.service.dto.TagDto;
import com.epam.ems.service.exception.DuplicateEntityException;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.web.hateoas.Hateoas;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.core.LastInvocationAware;
import org.springframework.hateoas.server.core.MethodInvocation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    private final Hateoas<TagDto> hateoas;

    @Autowired
    public TagController(TagService tagService, Hateoas<TagDto> hateoas){
        this.tagService = tagService;
        this.hateoas = hateoas;
    }

    @GetMapping
    public CollectionModel<TagDto> getTags(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "5") @Min(1) int elements) {

        List<TagDto> res = tagService.getAll(page,elements);
        res.forEach(hateoas::buildHateoas);
        return hateoas.buildPaginationModel(res,
                ()->page < 2 ? null : methodOn(getClass()).getTags(1,elements),
                ()->res.size() < elements ? null : methodOn(getClass()).getTags(page+1, elements),
                ()->page < 2 ? null : methodOn(getClass()).getTags(page-1, elements));
    }

    @GetMapping("/{id}")
    public TagDto getTagById(
            @PathVariable("id") @Min(value = 1, message = "msg.id.negative") long id)
            throws NoSuchEntityException {
        return hateoas.buildHateoas(tagService.getById(id));
    }

    @GetMapping("/widest")
    public TagDto getMostWidelyUsedTag(){
        return hateoas.buildHateoas(tagService.retrieveMostUsedTagOfUserWithLargestOrderCost());
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto createTag(
            @NotNull(message = "msg.dto.null") @Valid @RequestBody  TagDto tagDto) throws DuplicateEntityException {
        return hateoas.buildHateoas(tagService.insert(tagDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTag(
            @PathVariable("id") @Min(value = 1, message = "msg.id.negative") long id) throws NoSuchEntityException{
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
