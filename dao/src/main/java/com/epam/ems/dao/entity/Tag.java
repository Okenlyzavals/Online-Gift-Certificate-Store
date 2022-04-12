package com.epam.ems.dao.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@Builder
public class Tag{
    protected Long id;
    private String name;
}
