package com.epam.ems.web.controller;

import com.epam.ems.service.TagService;
import com.epam.ems.service.dto.TagDto;
import com.epam.ems.service.validation.OnCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
@RequestMapping(value = "/tags")
@ComponentScan("com.epam.ems.service.impl")
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagDto> getTags(){
        return tagService.getAll();
    }

    @GetMapping("/{id}")
    public TagDto getTagById(@PathVariable("id") @Min(1L) @NotNull long id){
        return tagService.getById(id);
    }

    @PostMapping(value = "/new",consumes = "application/json")
    public ResponseEntity createTag(
            @Valid @RequestBody TagDto tagDto){
        tagService.insert(tagDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteTag(@PathVariable("id") @Min(1L) @NotNull long id){
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
