package com.epam.ems.dao.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class Entity {
    protected Long id;
}
