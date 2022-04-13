package com.epam.ems.service;

import com.epam.ems.service.dto.TagDto;

public interface TagService extends AbstractService<TagDto> {

    TagDto getByName(String name);
}
